package msifeed.mc.more.crabs.action;

import org.junit.Test;

public class TestActionSerialization {
    private static final String hardHitJson = "{\n" +
            "  \"name\": \"hard_hit\",\n" +
            "  \"title\": \"Сильный удар\",\n" +
            "  \"tags\": [\"melee\"],\n" +
            "  \"score\": [\"g40\", \"str\"],\n" +
            "  \"target\": [\"damage\", \"const_damage:5\"],\n" +
            "  \"self\": [\"score%:0.9\"]\n" +
            "}";
    private static final String stunJson = "{\n" +
            "  \"name\": \"stun\",\n" +
            "  \"title\": \"Оглушение\",\n" +
            "  \"tags\": [\"melee\"],\n" +
            "  \"combo\": [\"hard_hit\", \"hard_hit\"],\n" +
            "  \"score\": [\"g40\", \"str\"],\n" +
            "  \"target\": [\"damage\", \"buff:0:0:skip\"],\n" +
            "  \"self\": []\n" +
            "}";

    @Test
    public void pomf() {

    }
}
