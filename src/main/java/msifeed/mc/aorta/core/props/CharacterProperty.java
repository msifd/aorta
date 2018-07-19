package msifeed.mc.aorta.core.props;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.props.ExtProp;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class CharacterProperty extends ExtProp {
    static final String PROP_NAME = Aorta.MODID + ".core.char";

    public Character character = null;

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

}
