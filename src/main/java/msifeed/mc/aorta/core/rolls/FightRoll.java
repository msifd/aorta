package msifeed.mc.aorta.core.rolls;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.character.CharStatus;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;

import java.util.List;
import java.util.stream.Stream;

public class FightRoll extends Roll {
    public FightAction action;

    public FightRoll(Character character, CharStatus status, FightAction action) {
        final List<Double> factors = Aorta.DEFINES.rules().modifiers.get(action);
        final double featSum = Stream.of(Feature.values())
                .mapToDouble(f -> Dices.feature(character.features.get(f) + status.modifiers.feat(f)) * factors.get(f.ordinal()))
                .sum();

        this.action = action;
        this.mods = status.modifiers;
        this.sanity = Roll.sanityMod(status.sanity);
        this.roll = (int) Math.floor(featSum);
        this.result = roll + mods.rollMod + sanity;
        this.critical = Critical.roll();
    }
}
