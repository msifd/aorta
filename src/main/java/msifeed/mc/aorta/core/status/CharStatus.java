package msifeed.mc.aorta.core.status;

import msifeed.mc.aorta.core.character.BodyPart;

import java.util.HashMap;

public class CharStatus {
    public HashMap<BodyPart, Integer> damage = new HashMap<>();
    public HashMap<BodyPart, Integer> armor = new HashMap<>();
    public int shields;
}
