package msifeed.mc.aorta.core.props;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.props.ExtProp;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class CharacterProperty extends ExtProp {
    private static final String PROP_NAME = Aorta.MODID + ".core.char";

    public Character character;

    public static CharacterProperty get(EntityLivingBase entity) {
        return (CharacterProperty) entity.getExtendedProperties(PROP_NAME);
    }

    @Override
    public String getName() {
        return PROP_NAME;
    }

    @Override
    public void init(Entity entity, World world) {
        if (entity instanceof EntityPlayer && character == null) {
            character = new Character();
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        if (character == null)
            return;
        compound.setTag(PROP_NAME, character.toNBT());
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (!compound.hasKey(PROP_NAME))
            return;
        if (character == null)
            character = new Character();
        character.fromNBT(compound.getCompoundTag(PROP_NAME));
    }

    public static class Handler {
        @SubscribeEvent
        public void onEntityConstruct(EntityEvent.EntityConstructing e) {
            if (e.entity instanceof EntityLivingBase)
                e.entity.registerExtendedProperties(PROP_NAME, new CharacterProperty());
        }

        @SubscribeEvent
        public void entityJoinWorld(EntityJoinWorldEvent e) {
            if (!e.world.isRemote && e.entity instanceof EntityLivingBase) {
                final CharacterProperty prop = get((EntityLivingBase) e.entity);
                prop.sync(e.world, e.entity);
            }
        }

        @SubscribeEvent
        public void playerStartedTracking(PlayerEvent.StartTracking e) {
            if (!e.target.worldObj.isRemote && e.target instanceof EntityLivingBase) {
                final CharacterProperty prop = get((EntityLivingBase) e.target);
                prop.sync(e.entityPlayer, e.target);
            }
        }
    }
}
