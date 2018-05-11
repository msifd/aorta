package msifeed.mc.aorta.core.character;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.network.EntityPropertySync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class CharacterProperty implements EntityPropertySync.ISyncProp {
    public final Character character = new Character();

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new Handler());
    }

    public static CharacterProperty get(Entity e) {
        return (CharacterProperty) e.getExtendedProperties(Tags.PROP_NAME);
    }

    public static void addProp(Entity e) {
        if (e.getExtendedProperties(Tags.PROP_NAME) == null)
            e.registerExtendedProperties(Tags.PROP_NAME, new CharacterProperty());
    }

    @Override
    public String getName() {
        return Tags.PROP_NAME;
    }

    @Override
    public void init(Entity entity, World world) {
        loadNBTData(entity.getEntityData());
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        final NBTTagCompound prop = new NBTTagCompound();

        final Feature[] featureEnum = Feature.values();
        final byte[] features = new byte[featureEnum.length];
        for (int i = 0; i < features.length; i++) {
            final Grade grade = character.features.getOrDefault(featureEnum[i], Grade.NORMAL);
            features[i] = (byte) grade.ordinal();
        }
        prop.setByteArray(Tags.FEATURES, features);

        compound.setTag(Tags.PROP_NAME, prop);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (!compound.hasKey(Tags.PROP_NAME))
            return;

        final NBTTagCompound prop = compound.getCompoundTag(Tags.PROP_NAME);

        final byte[] features = prop.getByteArray(Tags.FEATURES);
        final Feature[] featureEnum = Feature.values();
        final Grade[] gradeEnum = Grade.values();
        for (int i = 0; i < features.length; i++) {
            character.features.put(featureEnum[i], gradeEnum[features[i]]);
        }
    }

    public static class Handler {
        @SubscribeEvent
        public void onEntityConstruct(EntityEvent.EntityConstructing e) {
            if (e.entity instanceof EntityPlayer) {
                addProp(e.entity);
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
    }
}
