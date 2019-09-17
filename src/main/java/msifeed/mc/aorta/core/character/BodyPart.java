package msifeed.mc.aorta.core.character;

import net.minecraft.nbt.NBTTagCompound;

public class BodyPart {
    public String name;
    public short maxHealth;
    public boolean vital;

    public short health;
    public short armor;

    public BodyPart() {
    }

    public BodyPart(BodyPart bp) {
        this(bp.name, bp.maxHealth, bp.vital);
        health = bp.health;
        armor = bp.armor;
    }

    public BodyPart(NBTTagCompound nbt) {
        fromNBT(nbt);
    }

    public BodyPart(String name, int maxHealth, boolean vital) {
        this.name = name;
        this.maxHealth = (short) maxHealth;
        this.vital = vital;
        this.health = this.maxHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public boolean isInjured() {
        final int percent = 100 * health / maxHealth;
        return percent <= 20;
    }

    public boolean isVitalGone() {
        return vital && health <= 0;
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound c = new NBTTagCompound();
        c.setString("name", name);
        c.setShort("max", maxHealth);
        c.setBoolean("vital", vital);
        c.setShort("hp", health);
        c.setShort("armor", armor);
        return c;
    }

    public void fromNBT(NBTTagCompound c) {
        name = c.getString("name");
        maxHealth = c.getShort("max");
        vital = c.getBoolean("vital");
        health = c.getShort("hp");
        armor = c.getShort("armor");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BodyPart))
            return false;
        final BodyPart b = (BodyPart) o;
        return b.name.equals(name)
                && b.maxHealth == maxHealth
                && b.vital == vital;
    }

    @Override
    public String toString() {
        // 'head' vital 12/12 [0]
        // 'left hand' 10/15 [3]
        return String.format("'%s' %s%d/%d [%d]", name, vital ? "vital " : "", health, maxHealth, armor);
    }
}
