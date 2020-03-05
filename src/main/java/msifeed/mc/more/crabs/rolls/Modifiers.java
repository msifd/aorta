package msifeed.mc.more.crabs.rolls;

import msifeed.mc.more.crabs.character.Ability;

import java.util.EnumMap;

public class Modifiers {
    public int roll = 0;
    public EnumMap<Ability, Integer> features = new EnumMap<>(Ability.class);

    public Modifiers() {
    }

    public Modifiers(Modifiers m) {
        roll = m.roll;
        features.putAll(m.features);
    }

    public boolean isZeroed() {
        return roll == 0 && !hasAbilityMods();
    }

    public boolean hasAbilityMods() {
        return features.values().stream().anyMatch(i -> i != 0);
    }

    public int feat(Ability ability) {
        return features.getOrDefault(ability, 0);
    }
}
