package msifeed.mc.aorta.core.rolls;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.meta.MetaInfo;

import java.util.stream.Stream;

public class FeatureRoll extends Roll {
    public Feature feature;

    public FeatureRoll(Character character, MetaInfo meta, String target, Feature feature) {
        super(character, meta, target);

        final double rollAvg = Stream.of(feature.feats)
                .map(f -> character.features.get(f) + meta.modifiers.feat(f))
                .mapToDouble(Dices::feature)
                .sum() / feature.feats.length;

        this.feature = feature;
        this.roll = Dices.randRound(rollAvg);
        this.result = roll + mods.rollMod + statusMod;
    }

    public boolean check(int difficulty) {
        return critical == Critical.LUCK || critical != Critical.FAIL && result >= difficulty;
    }
}
