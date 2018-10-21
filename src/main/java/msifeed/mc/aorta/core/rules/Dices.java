package msifeed.mc.aorta.core.rules;

import java.util.Random;

public class Dices {
    private final static Random random = new Random();

    public static int dice(int n) {
        return random.nextInt(n) + 1;
    }
}
