package msifeed.mc.aorta.core.status;

import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CharStatus {
    public HashMap<String, BodyPartHealth> health = new HashMap<>();
    public BodyShield shield = new BodyShield();
    public byte sanity = 100;

    public CharStatus() {

    }

    public CharStatus(CharStatus s) {
        for (Map.Entry<String, BodyPartHealth> e : s.health.entrySet())
            health.put(e.getKey(), new BodyPartHealth(e.getValue()));
        shield = new BodyShield(s.shield);
        sanity = s.sanity;
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound c = new NBTTagCompound();

        final NBTTagCompound hc = new NBTTagCompound();
        for (Map.Entry<String, BodyPartHealth> e : health.entrySet()) {
            hc.setInteger(e.getKey(), e.getValue().toInt());
        }
        c.setTag(Tags.health, hc);
        c.setTag(Tags.shield, shield.toNBT());
        c.setByte(Tags.sanity, sanity);

        return c;
    }

    public void fromNBT(NBTTagCompound compound) {
        health.clear();
        final NBTTagCompound hc = compound.getCompoundTag(Tags.health);
        for (String k : (Set<String>) hc.func_150296_c()) {
            final BodyPartHealth h = new BodyPartHealth();
            h.fromInt(hc.getInteger(k));
            health.put(k, h);
        }

        shield.fromNBT(compound.getCompoundTag(Tags.shield));
        sanity = compound.getByte(Tags.sanity);
    }

    private static class Tags {
        static final String health = "health";
        static final String shield = "shield";
        static final String sanity = "sanity";
    }
}
