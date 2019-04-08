package msifeed.mc.aorta.core.character;

import net.minecraft.nbt.NBTTagCompound;

public class BodyPart implements Comparable {
    public String name;
    public Type type;
    public short max;
    public boolean fatal;

    public BodyPart() {
    }

    public BodyPart(BodyPart bp) {
        this.name = bp.name;
        this.type = bp.type;
        this.max = bp.max;
        this.fatal = bp.fatal;
    }

    public BodyPart(String name, Type type, int max, boolean fatal) {
        this.name = name;
        this.type = type;
        this.max = (short) max;
        this.fatal = fatal;
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
                && b.max == max
                && b.fatal == fatal;
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setString("value", name);
        compound.setByte("type", (byte) type.ordinal());
        compound.setShort("max", max);
        compound.setBoolean("fatal", fatal);
        return compound;
    }

    public void fromNBT(NBTTagCompound compound) {
        name = compound.getString("value");
        type = Type.values()[compound.getByte("type")];
        max = compound.getShort("max");
        fatal = compound.getBoolean("fatal");
    }

    public String toLineString() {
        // head [head] 25 fatal
        return String.format("%s [%s] %d%s", name, type.toString().toLowerCase(), max, fatal ? " fatal" : "");
    }

    public enum Type {
        HEAD, BODY, HAND, LEG, OTHER
    }
}
