package msifeed.mc.aorta.core.character;

import msifeed.mc.aorta.props.INBTSerializable;
import net.minecraft.nbt.NBTTagCompound;

public class BodyPart implements INBTSerializable {
    public String name;
    public Type type;
    public short max;
    public short disfunction;
    public short death;

    public BodyPart() {
    }

    public BodyPart(String name, Type type, int max, int disfunction, int death) {
        this.name = name;
        this.type = type;
        this.max = (short) max;
        this.disfunction = (short) disfunction;
        this.death = (short) death;
    }

    @Override
    public NBTTagCompound toNBT() {
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setString("name", name);
        compound.setByte("type", (byte) type.ordinal());
        compound.setShort("max", max);
        compound.setShort("disfunction", disfunction);
        compound.setShort("death", death);
        return compound;
    }

    @Override
    public void fromNBT(NBTTagCompound compound) {
        name = compound.getString("name");
        type = Type.values()[compound.getByte("type")];
        max = compound.getShort("max");
        disfunction = compound.getShort("disfunction");
        death = compound.getShort("death");
    }

    public String toLineString() {
        // head [head] 25/5/0
        return String.format("%s [%s] %d/%d/%d", name, type.toString().toLowerCase(), max, disfunction, death);
    }

    public enum Type {
        OTHER, HEAD, BODY, HAND, LEG
    }
}
