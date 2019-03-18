package msifeed.mc.aorta.core.character;

public class BodyPartHealth {
    public short health;
    public short armor;

    public BodyPartHealth() {
    }

    public BodyPartHealth(BodyPartHealth h) {
        this(h.health, h.armor);
    }

    public BodyPartHealth(int d, int a) {
        this.health = (short) d;
        this.armor = (short) a;
    }

    public boolean isInjured(BodyPart bodyPart) {
        final int percent = (100 * health) / bodyPart.max;
        return percent <= 20;
    }

    public short getHealth() {
        return health;
    }

    public int toInt() {
        return (health << Short.SIZE) | armor;
    }

    public void fromInt(int i) {
        health = (short) (i >> Short.SIZE);
        armor = (short) i;
    }

    @Override
    public String toString() {
        return "h: " + health + ", a: " + armor;
    }
}
