package msifeed.mc.aorta.core.character;

import msifeed.mc.aorta.utils.L10n;
import net.minecraft.nbt.NBTTagCompound;

public class BodyShield {
    public Type type = Type.NONE;
    public short power;

    public BodyShield() {
    }

    public BodyShield(BodyShield s) {
        type = s.type;
        power = s.power;
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound c = new NBTTagCompound();
        c.setByte(Tags.TYPE, (byte) type.ordinal());
        c.setShort(Tags.POWER, power);
        return c;
    }

    public void fromNBT(NBTTagCompound c) {
        type = Type.values()[c.getByte(Tags.TYPE)];
        power = c.getShort(Tags.POWER);
    }

    @Override
    public String toString() {
        return type.toString() + " = " + power;
    }

    public enum Type {
        NONE, ENERGY, KINNALIAN, PSIONIC, INDUSTRIAL;

        public String tr() {
            return L10n.tr("aorta.shield." + name().toLowerCase());
        }
    }

    private static class Tags {
        static final String TYPE = "type";
        static final String POWER = "power";
    }
}
