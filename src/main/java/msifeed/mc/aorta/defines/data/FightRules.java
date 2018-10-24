package msifeed.mc.aorta.defines.data;

import msifeed.mc.aorta.core.rules.FightAction;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class FightRules {
    public Map<FightAction, ArrayList<Double>> modifiers = new EnumMap<>(FightAction.class);
}
