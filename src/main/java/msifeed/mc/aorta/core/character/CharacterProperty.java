package msifeed.mc.aorta.core.character;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.props.ISyncableExtProp;
import msifeed.mc.aorta.props.SyncProp;
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
import java.util.Optional;

public class CharacterProperty implements ISyncableExtProp {
    private Character character;

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new Handler());
    }

    public static CharacterProperty get(EntityLivingBase e) {
        return (CharacterProperty) e.getExtendedProperties(Tags.PROP_NAME);
    }

    public Optional<Character> getCharacter() {
        return Optional.ofNullable(character);
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
        if (character == null)
            return;
        compound.setTag(Tags.PROP_NAME, character.toNBT());
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (!compound.hasKey(Tags.PROP_NAME))
            return;
        if (character == null)
            character = new Character();
        character.fromNBT(compound.getCompoundTag(Tags.PROP_NAME));
    }

    public static class Handler {
        @SubscribeEvent
        public void onEntityConstruct(EntityEvent.EntityConstructing e) {
            if (!(e.entity instanceof EntityLivingBase))
                return;

            if (e.entity.getExtendedProperties(Tags.PROP_NAME) == null) {
                final CharacterProperty prop = new CharacterProperty();
                if (e.entity instanceof EntityPlayer)
                    prop.character = CharacterFactory.forEntity((EntityLivingBase) e.entity);
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
            if (!(e.entity instanceof EntityLivingBase))
                return;
            final CharacterProperty prop = get((EntityLivingBase) e.entity);
            if (prop != null)
                SyncProp.sync(e.world, e.entity, prop);
        }

        @SubscribeEvent
        public void playerStartedTracking(PlayerEvent.StartTracking e) {
            if (!(e.target instanceof EntityLivingBase))
                return;
            final CharacterProperty prop = get((EntityLivingBase) e.target);
            if (prop != null)
                SyncProp.sync((EntityPlayerMP) e.entityPlayer, e.target, prop);
        }
    }

    private static class Tags {
        static final String PROP_NAME = Aorta.MODID + ".core.char";
    }
}
