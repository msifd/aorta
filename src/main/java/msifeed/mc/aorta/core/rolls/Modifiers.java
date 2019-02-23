package msifeed.mc.aorta.core.rolls;

import msifeed.mc.aorta.core.character.Feature;

import java.io.Serializable;
import java.util.EnumMap;

public class Modifiers implements Serializable {
    public int rollMod = 0;
    public EnumMap<Feature, Integer> featureMods = new EnumMap<>(Feature.class);

    public Modifiers() {
    }

    public Modifiers(Modifiers m) {
        rollMod = m.rollMod;
        featureMods.putAll(m.featureMods);
    }

    public boolean isZeroed() {
        return rollMod == 0 && featureMods.isEmpty();
    }

    public int feat(Feature feature) {
        return featureMods.getOrDefault(feature, 0);
    }
}
