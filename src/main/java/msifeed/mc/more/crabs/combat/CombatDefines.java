package msifeed.mc.more.crabs.combat;

import msifeed.mc.more.crabs.character.Ability;

import java.util.HashMap;

public class CombatDefines {
    public DamageSettings damageSettings = new DamageSettings();
    public ArmorPenalty armorPenalty = new ArmorPenalty();

    public static class DamageSettings {
        public float armorResistancePerPoint = 0.025f;
        public float maxArmorResistance = 0.8f;
        public float minFinalDamage = 0.2f;

        public float applyArmor(float basicDamage, int armor, int damageThreshold) {
            final float armorResistance = armor * armorResistancePerPoint;
            final float damageAfterArmor = basicDamage * (1f - Math.min(armorResistance, maxArmorResistance));
            return Math.max(damageAfterArmor - damageThreshold, basicDamage * minFinalDamage);
        }
    }

    public static class ArmorPenalty {
        public float onRoll = 0;
        public HashMap<Ability, Float> onStats = new HashMap<>();

        public ArmorPenalty() {
            for (Ability a : Ability.values())
                onStats.put(a, 0f);
        }
    }
}
