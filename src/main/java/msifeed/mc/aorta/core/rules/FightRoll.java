package msifeed.mc.aorta.core.rules;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.status.CharStatus;
import msifeed.mc.aorta.defines.data.FightRules;

import java.util.ArrayList;

public class FightRoll {
    public FightAction action;
    public int features;
    public int mod;
    public int sanity;
    public int result;

    public FightRoll(Character character, CharStatus status, FightAction action, int mod) {
        final ArrayList<FightRules.FeatMod> featMods = Aorta.DEFINES.rules().modifiers.get(action);
        final double featSum = featMods.stream()
                .mapToDouble(fm -> character.features.get(fm.f).getValue() * fm.c)
                .sum();
        final int sanity = SanityMod.calc(status);

        this.action = action;
        this.features = (int) Math.floor(featSum);
        this.mod = mod;
        this.sanity = sanity;
        this.result = features + mod + sanity;
    }
}
