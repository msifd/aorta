package msifeed.mc.aorta.core.rolls;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.meta.MetaInfo;

import java.util.stream.Stream;

public class FeatureRoll extends Roll {
    public Feature[] features;

    public FeatureRoll(Character character, MetaInfo meta, Feature... feats) {
        super(character, meta);

        final double rollAvg = Stream.of(feats)
                .map(f -> character.features.get(f) + meta.modifiers.feat(f))
                .mapToDouble(Dices::feature)
                .sum() / feats.length;

        this.features = feats;
        this.roll = Dices.randRound(rollAvg);
        this.result = roll + mods.rollMod + statusMod;
    }

    public boolean check(int difficulty) {
        return critical == Critical.LUCK || critical != Critical.FAIL && result >= difficulty;
    }
}
