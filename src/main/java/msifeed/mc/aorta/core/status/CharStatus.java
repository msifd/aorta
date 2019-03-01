package msifeed.mc.aorta.core.status;

import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.rolls.Modifiers;
import net.minecraft.nbt.NBTTagCompound;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CharStatus {
    public Map<String, BodyPartHealth> health = new LinkedHashMap<>();
    public BodyShield shield = new BodyShield();
    public byte sanity = 100;
    public byte psionics = 0;
    public Modifiers modifiers = new Modifiers();

    public CharStatus() {
    }

    public CharStatus(CharStatus s) {
        for (Map.Entry<String, BodyPartHealth> e : s.health.entrySet())
            health.put(e.getKey(), new BodyPartHealth(e.getValue()));
        shield = new BodyShield(s.shield);
        sanity = s.sanity;
        psionics = s.psionics;
        modifiers = new Modifiers(s.modifiers);
    }

    public int countVitality(int vitalityThreshold) {
        final int currentHealth = health.values().stream().mapToInt(BodyPartHealth::getHealth).sum();
        return Math.max(0, Math.min(vitalityThreshold, currentHealth - vitalityThreshold));
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound c = new NBTTagCompound();

        final NBTTagCompound hc = new NBTTagCompound();
        for (Map.Entry<String, BodyPartHealth> e : health.entrySet()) {
            hc.setInteger(e.getKey(), e.getValue().toInt());
        }
        final NBTTagCompound mc = new NBTTagCompound();
        for (Map.Entry<Feature, Integer> e : modifiers.featureMods.entrySet()) {
            mc.setInteger(e.getKey().toString(), e.getValue());
        }

        c.setTag(Tags.health, hc);
        c.setTag(Tags.shield, shield.toNBT());
        c.setByte(Tags.sanity, sanity);
        c.setByte(Tags.psionics, psionics);
        c.setInteger(Tags.modifiersRoll, modifiers.rollMod);
        c.setTag(Tags.modifiers, mc);

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

        modifiers.rollMod = compound.getInteger(Tags.modifiersRoll);
        final NBTTagCompound mc = compound.getCompoundTag(Tags.modifiers);
        modifiers.featureMods.clear();
        for (String k : (Set<String>) mc.func_150296_c()) {
            modifiers.featureMods.put(Feature.valueOf(k), mc.getInteger(k));
        }

        shield.fromNBT(compound.getCompoundTag(Tags.shield));
        sanity = compound.getByte(Tags.sanity);
        psionics = compound.getByte(Tags.psionics);
    }

    private static class Tags {
        static final String health = "health";
        static final String shield = "shield";
        static final String sanity = "sanity";
        static final String psionics = "psionics";
        static final String modifiers = "mods";
        static final String modifiersRoll = "roll";
    }
}
