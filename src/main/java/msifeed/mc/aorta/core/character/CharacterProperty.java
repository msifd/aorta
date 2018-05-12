package msifeed.mc.aorta.core.character;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.network.EntityPropertySync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import javax.annotation.Nullable;

public class CharacterProperty implements EntityPropertySync.ISyncProp {
    private Character character;

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new Handler());
    }

    public static CharacterProperty get(Entity e) {
        return (CharacterProperty) e.getExtendedProperties(Tags.PROP_NAME);
    }

    @Nullable
    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    @Override
    public String getName() {
        return Tags.PROP_NAME;
    }

    @Override
    public void init(Entity entity, World world) {
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        final NBTTagCompound prop = new NBTTagCompound();

        if (character == null)
            return;

        if (!character.features.isEmpty()) {
            final Feature[] featureEnum = Feature.values();
            final byte[] features = new byte[featureEnum.length];
            for (int i = 0; i < features.length; i++) {
                final Grade grade = character.features.getOrDefault(featureEnum[i], Grade.NORMAL);
                features[i] = (byte) grade.ordinal();
            }
            prop.setByteArray(Tags.FEATURES, features);
        }

        if (!character.bodyParts.isEmpty()) {
            final byte[] bodyParts = new byte[character.bodyParts.size() * 3]; // [3-byte parts]
            int bpByte = 0;
            for (BodyPart bp : character.bodyParts) {
                bodyParts[bpByte] = (byte) bp.type.ordinal();
                bodyParts[bpByte + 1] = bp.hits;
                bodyParts[bpByte + 2] = bp.maxHits;
                bpByte += 3;
            }
            compound.setByteArray(Tags.BODY_PARTS, bodyParts);
        }

        compound.setTag(Tags.PROP_NAME, prop);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (!compound.hasKey(Tags.PROP_NAME))
            return;

        final NBTTagCompound prop = compound.getCompoundTag(Tags.PROP_NAME);

        if (prop.hasKey(Tags.FEATURES)) {
            final byte[] features = prop.getByteArray(Tags.FEATURES);
            final Feature[] featureEnum = Feature.values();
            final Grade[] gradeEnum = Grade.values();
            for (int i = 0; i < features.length; i++) {
                character.features.put(featureEnum[i], gradeEnum[features[i]]);
            }
        }

        if (prop.hasKey(Tags.BODY_PARTS)) {
            final BodyPart.Type[] bpTypes = BodyPart.Type.values();
            final byte[] bodyParts = prop.getByteArray(Tags.BODY_PARTS);
            for (int bpByte = 0; bpByte < bodyParts.length; bpByte += 3) {
                final BodyPart bp = new BodyPart();
                bp.type = bpTypes[bodyParts[bpByte]];
                bp.hits = bodyParts[bpByte + 1];
                bp.maxHits = bodyParts[bpByte + 2];
                character.bodyParts.add(bp);
            }
        }
    }


    public static class Handler {
        @SubscribeEvent
        public void onEntityConstruct(EntityEvent.EntityConstructing e) {
            if (!(e.entity instanceof EntityLivingBase))
                return;

            if (e.entity.getExtendedProperties(Tags.PROP_NAME) == null) {
                final CharacterProperty prop = new CharacterProperty();
                if (e.entity instanceof EntityPlayer)
                    prop.character = CharacterFactory.forEntity(e.entity);
                e.entity.registerExtendedProperties(Tags.PROP_NAME, prop);
            }
        }

        @SubscribeEvent
        public void onClonePlayer(PlayerEvent.Clone e) {
            if (e.wasDeath) {
                final NBTTagCompound compound = new NBTTagCompound();
                CharacterProperty.get(e.original).saveNBTData(compound);
                CharacterProperty.get(e.entityPlayer).loadNBTData(compound);
            }
        }

        @SubscribeEvent
        public void entityJoinWorld(EntityJoinWorldEvent e) {
            CharacterProperty prop = get(e.entity);
            if (prop != null)
                EntityPropertySync.sync(e.world, e.entity, prop);
        }

        @SubscribeEvent
        public void playerStartedTracking(PlayerEvent.StartTracking e) {
            CharacterProperty prop = get(e.target);
            if (prop != null)
                EntityPropertySync.sync((EntityPlayerMP) e.entityPlayer, e.target, prop);
        }
    }

    private static class Tags {
        static final String PROP_NAME = Aorta.MODID + ".core.char";
        static final String FEATURES = "features";
        static final String BODY_PARTS = "bodyParts";
    }
}
