package msifeed.mc.aorta.core.rolls;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.meta.MetaInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FightRoll extends Roll {
    public FightAction action;

    public FightRoll(Character character, MetaInfo meta, FightAction action) {
        super(character, meta);

        final List<Double> factors = Aorta.DEFINES.rules().modifiers.get(action);
        final double featSum = Stream.of(Feature.values())
                .mapToDouble(f -> Dices.feature(character.features.get(f) + meta.modifiers.feat(f)) * factors.get(f.ordinal()))
                .sum();

        this.action = action;
        this.roll = (int) Math.floor(featSum);
        rules(character, meta);

        this.result = roll + mods.rollMod + statusMod;
    }

    private void rules(Character character, MetaInfo meta) {
        // Load mod
        switch (action) {
            case SHOT:
            case SPECIAL_SHOT:
            case DODGE:
                statusMod += loadMod(character.load);
                break;
        }
    }

    public static int loadMod(int load) {
        for (Map.Entry<Integer, Integer> e : Aorta.DEFINES.rules().loadMod.entrySet())
            if (load <= e.getKey())
                return e.getValue();
        return -999;
    }
}
