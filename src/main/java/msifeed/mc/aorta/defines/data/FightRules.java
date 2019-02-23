package msifeed.mc.aorta.defines.data;

import msifeed.mc.aorta.core.rolls.FightAction;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class FightRules {
    public Map<FightAction, List<Double>> modifiers = new EnumMap<>(FightAction.class);
}
