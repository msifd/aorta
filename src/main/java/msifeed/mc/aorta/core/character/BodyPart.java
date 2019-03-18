package msifeed.mc.aorta.core.character;

import net.minecraft.nbt.NBTTagCompound;

public class BodyPart implements Comparable {
    public String name;
    public Type type;
    public short max;

    public BodyPart() {
    }

    public BodyPart(BodyPart bp) {
        this.name = bp.name;
        this.type = bp.type;
        this.max = bp.max;
    }

    public BodyPart(String name, Type type, int max) {
        this.name = name;
        this.type = type;
        this.max = (short) max;
    }

    public short getMaxHealth() {
        return max;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof BodyPart))
            return 1;
        return type.compareTo(((BodyPart) o).type);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BodyPart))
            return false;
        final BodyPart b = (BodyPart) o;
        return b.name.equals(name)
                && b.type == type
                && b.max == max;
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setString("value", name);
        compound.setByte("type", (byte) type.ordinal());
        compound.setShort("max", max);
        return compound;
    }

    public void fromNBT(NBTTagCompound compound) {
        name = compound.getString("value");
        type = Type.values()[compound.getByte("type")];
        max = compound.getShort("max");
    }

    public String toLineString() {
        // head [head] 25
        return String.format("%s [%s] %d", name, type.toString().toLowerCase(), max);
    }

    public enum Type {
        HEAD, BODY, HAND, LEG, OTHER
    }
}
