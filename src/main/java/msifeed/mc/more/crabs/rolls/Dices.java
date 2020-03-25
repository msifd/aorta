package msifeed.mc.more.crabs.rolls;

import java.util.Random;

public class Dices {
    private static final Random rand = new Random();

    public static int n3d7m3() {
        return dice(7) + dice(7) + dice(7) - 3;
    }

    public static int d20() {
        return dice(20);
    }

    public static int dice(int n) {
        return rand.nextInt(n) + 1;
    }

    public static double gauss(double mean, int from, int to) {
        final double std_dev = from / 2. + to / 2.;
        double roll = from - 1;
        while (roll < from || roll > to) {
            roll = rand.nextGaussian() * mean + std_dev;
        }
        return roll;
    }

    public static Criticalness critical() {
        final int roll = dice(20);
        if (roll == 0) return Criticalness.FAIL;
        if (roll == 1) return Criticalness.LUCK;
        return Criticalness.NONE;
    }
}
