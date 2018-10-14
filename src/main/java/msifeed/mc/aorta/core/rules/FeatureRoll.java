package msifeed.mc.aorta.core.rules;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;

public class FeatureRoll {
    public static int roll(Character c, Feature f, int mod) {
        return c.features.get(f).getValue() + mod;
    }
}
