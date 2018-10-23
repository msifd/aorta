package msifeed.mc.aorta.defines.data;

import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.rules.FightAction;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class FightRules {
    public Map<FightAction, ArrayList<FeatMod>> modifiers = new EnumMap<>(FightAction.class);

    public static class FeatMod {
        public Feature f;
        public double c;
    }
}
