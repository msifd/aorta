package msifeed.mc.more.crabs.combat;

import msifeed.mc.more.crabs.character.Ability;

import java.util.HashMap;

public class CombatDefines {
    public ArmorPenalty armorPenalty = new ArmorPenalty();

    public static class ArmorPenalty {
        public float onRoll = 0;
        public HashMap<Ability, Float> onStats = new HashMap<>();

        public ArmorPenalty() {
            for (Ability a : Ability.values())
                onStats.put(a, 0f);
        }
    }
}
