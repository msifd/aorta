package msifeed.mc.more.crabs.rolls;

import msifeed.mc.more.crabs.character.Ability;

import java.io.Serializable;
import java.util.EnumMap;

public class Modifiers implements Serializable {
    public int roll = 0;
    public EnumMap<Ability, Integer> features = new EnumMap<>(Ability.class);

    public Modifiers() {
    }

    public Modifiers(Modifiers m) {
        roll = m.roll;
        features.putAll(m.features);
    }

    public boolean isZeroed() {
        return roll == 0 && features.isEmpty();
    }

    public int feat(Ability ability) {
        return features.getOrDefault(ability, 0);
    }
}
