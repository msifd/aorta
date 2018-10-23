package msifeed.mc.aorta.core.rules;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.status.CharStatus;

public class FeatureRoll {
    public Feature feature;
    public int mod;
    public int sanity;
    public int result;

    public FeatureRoll(Character c, CharStatus status, Feature f, int mod) {
        final int sanity = SanityMod.calc(status);
        final int feat = c.features.get(f).getValue();
        final int offset = feat * 2;
        final int flatDice = Dices.dice(20 - offset - 3);
        final int normalDice = Dices.dice(4) - Dices.dice(3);

        this.feature = f;
        this.mod = mod;
        this.sanity = sanity;
        this.result = normalDice + flatDice + offset + mod + sanity;
    }
}
