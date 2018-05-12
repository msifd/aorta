package msifeed.mc.aorta.core.character;

import java.util.EnumMap;
import java.util.HashSet;

public class Character {
    public EnumMap<Feature, Grade> features = new EnumMap<>(Feature.class);
    public HashSet<BodyPart> bodyParts = new HashSet<>();
}
