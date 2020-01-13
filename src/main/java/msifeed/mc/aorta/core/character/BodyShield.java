package msifeed.mc.aorta.core.character;

import msifeed.mc.sys.utils.L10n;

public class BodyShield {
    public Type type = Type.NONE;
    public short power;

    public int pack() {
        return (type.ordinal() << Short.SIZE) | power;
    }

    public void unpack(int i) {
        type = Type.values()[i >> Short.SIZE];
        power = (short) i;
    }

    @Override
    public String toString() {
        return type.toString() + " = " + power;
    }

    public enum Type {
        NONE, ENERGY, KINNALIAN, PSIONIC, INDUSTRIAL;

        @Override
        public String toString() {
            return L10n.tr("aorta.shield." + name().toLowerCase());
        }
    }
}
