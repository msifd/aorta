package msifeed.mc.aorta.defines;

import com.google.common.collect.ImmutableMap;
import msifeed.mc.aorta.core.rolls.FightAction;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class FightRules {
    public Map<FightAction, List<Double>> modifiers = new EnumMap<>(FightAction.class);
    public Map<Integer, Integer> loadMod = ImmutableMap.of(
            4, 0,
            8, -4,
            12, -8,
            20, -10
    );
}
