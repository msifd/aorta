package msifeed.mc.aorta.core.status;

public class BodyPartHealth {
    public short health;
    public short armor;

    public BodyPartHealth() {
    }

    public BodyPartHealth(BodyPartHealth h) {
        this(h.health, h.armor);
    }

    public BodyPartHealth(short d, short a) {
        this.health = d;
        this.armor = a;
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
