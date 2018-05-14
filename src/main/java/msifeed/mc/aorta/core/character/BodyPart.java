package msifeed.mc.aorta.core.character;

import msifeed.mc.aorta.props.INBTSerializable;
import net.minecraft.nbt.NBTTagCompound;

public class BodyPart implements INBTSerializable {
    public String name;
    public Type type;
    public short hits;
    public short maxHits;
    public short criticalToKO;
    public short criticalToDeath;

    public BodyPart() {
    }

    @Override
    public NBTTagCompound toNBT() {
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setString("name", name);
        compound.setShort("type", (short) type.ordinal());
        compound.setShort("hits", hits);
        compound.setShort("maxHits", maxHits);
        compound.setShort("critKO", criticalToKO);
        compound.setShort("critDeath", criticalToDeath);
        return compound;
    }

    @Override
    public void fromNBT(NBTTagCompound compound) {
        name = compound.getString("name");
        type = Type.values()[compound.getByte("type")];
        hits = compound.getShort("hits");
        maxHits = compound.getShort("maxHits");
        criticalToKO = compound.getShort("critKO");
        criticalToDeath = compound.getShort("critDeath");
    }

    public enum Type {
        UNKNOWN, HEAD, BODY, HAND, LEG
    }
}
