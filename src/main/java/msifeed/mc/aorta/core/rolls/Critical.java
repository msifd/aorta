package msifeed.mc.aorta.core.rolls;

public enum Critical {
    NONE, LUCK, FAIL;

    public static Critical roll() {
        switch (Dices.dice(20)) {
            case 1:
                return FAIL;
            case 20:
                return LUCK;
            default:
                return NONE;
        }
    }
}
