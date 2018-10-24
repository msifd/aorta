package msifeed.mc.aorta.core.rules;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.status.CharStatus;

import java.util.ArrayList;
import java.util.stream.Stream;

public class FightRoll {
    public FightAction action;
    public int roll;
    public int mod;
    public int sanity;
    public int result;

    public FightRoll(Character character, CharStatus status, FightAction action, int mod) {
        final ArrayList<Double> featMods = Aorta.DEFINES.rules().modifiers.get(action);
        final double featSum = Stream.of(Feature.values())
                .mapToDouble(f -> character.features.get(f).roll() * featMods.get(f.ordinal()))
                .sum();
        final int sanity = SanityMod.calc(status);

        this.action = action;
        this.roll = (int) Math.floor(featSum);
        this.mod = mod;
        this.sanity = sanity;
        this.result = roll + mod + sanity;
    }
}
