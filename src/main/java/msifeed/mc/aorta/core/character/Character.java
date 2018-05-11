package msifeed.mc.aorta.core.character;

import java.util.EnumMap;

public class Character {
    public EnumMap<Feature, Grade> features = new EnumMap<>(Feature.class);

    @Override
    public String toString() {
        String s = "Character { ";
        for (EnumMap.Entry<Feature, Grade> e : features.entrySet()) {
            s += e.getKey().toString().toLowerCase() + ": " + e.getValue().toString().toLowerCase() + ' ';
        }
        s += "}";
        return s;
    }
}
