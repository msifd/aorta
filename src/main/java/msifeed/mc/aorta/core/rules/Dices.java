package msifeed.mc.aorta.core.rules;

import java.util.Random;

public class Dices {
    private final static Random random = new Random();

    public static int dice(int n) {
        return random.nextInt(n) + 1;
    }

    public static int dice(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static int feature(int feature) {
        final int min = feature * 2 - 1;
        return dice(min, min + 5);
    }
}
