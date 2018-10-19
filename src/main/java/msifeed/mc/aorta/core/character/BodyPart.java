package msifeed.mc.aorta.core.character;

import net.minecraft.nbt.NBTTagCompound;

public class BodyPart {
    public String name;
    public Type type;
    public short max;
    public short disfunction;
    public boolean fatal;

    public BodyPart() {
    }

    public BodyPart(BodyPart bp) {
        this.name = bp.name;
        this.type = bp.type;
        this.max = bp.max;
        this.disfunction = bp.disfunction;
        this.fatal = bp.fatal;
    }

    public BodyPart(String name, Type type, int max, int disfunction, boolean fatal) {
        this.name = name;
        this.type = type;
        this.max = (short) max;
        this.disfunction = (short) disfunction;
        this.fatal = fatal;
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setString("name", name);
        compound.setByte("type", (byte) type.ordinal());
        compound.setShort("max", max);
        compound.setShort("disfunction", disfunction);
        compound.setBoolean("fatal", fatal);
        return compound;
    }

    public void fromNBT(NBTTagCompound compound) {
        name = compound.getString("name");
        type = Type.values()[compound.getByte("type")];
        max = compound.getShort("max");
        disfunction = compound.getShort("disfunction");
        fatal = compound.getBoolean("fatal");
    }

    public String toLineString() {
        // head [head] 25/5 fatal
        return String.format("%s [%s] %d/%d %s", name, type.toString().toLowerCase(), max, disfunction, fatal ? "fatal" : "");
    }

    public enum Type {
        OTHER, HEAD, BODY, HAND, LEG
    }
}
