package msifeed.mc.aorta.core.rolls;

public enum Critical {
    NONE, LUCK, FAIL;

    public static Critical roll() {
        switch (Dices.dice(40)) {
            case 1:
                return FAIL;
            case 2:
                return LUCK;
            default:
                return NONE;
        }
    }
}
