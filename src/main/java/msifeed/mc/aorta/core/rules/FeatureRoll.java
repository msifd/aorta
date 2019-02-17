package msifeed.mc.aorta.core.rules;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.status.CharStatus;

import java.util.stream.Stream;

public class FeatureRoll extends Roll {
    public Feature[] features;

    public FeatureRoll(Character c, CharStatus status, int mod, Feature... feats) {
        final double rollAvg = Stream.of(feats).mapToDouble(f -> c.features.get(f).roll()).sum() / feats.length;

        this.features = feats;
        this.roll = Dices.randRound(rollAvg);
        this.mod = mod;
        this.sanity = SanityMod.calc(status);
        this.result = roll + mod + sanity;
        this.critical = Critical.roll();
    }

    public boolean check(int difficulty) {
        return critical == Critical.LUCK || critical != Critical.FAIL && result >= difficulty;
    }
}
