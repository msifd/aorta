package msifeed.mc.aorta.core.rules;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.character.Grade;
import msifeed.mc.aorta.core.status.CharStatus;

public class FeatureRoll {
    public final Feature feature;
    public final int roll;
    public final int mod;
    public final int sanity;
    public final int result;

    public FeatureRoll(Character c, CharStatus status, Feature f, int mod) {
        final Grade grade = c.features.get(f);
        final int middleDice = Dices.dice(grade.value() * 2) - grade.value() * 2 + 3;
        final int normalDice = Dices.dice(4) - Dices.dice(3);

        this.feature = f;
        this.roll = grade.roll() + middleDice + normalDice;
        this.mod = mod;
        this.sanity = SanityMod.calc(status);
        this.result = roll + mod + sanity;
    }
}
