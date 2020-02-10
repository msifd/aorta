package msifeed.mc.more.crabs.rolls;

import java.util.Random;

public class Dices {
    private static final Random rand = new Random();

    public static int g15() {
        return (int) Math.floor(gauss(4.15, 1, 16));
    }

    public static int g20() {
        return (int) Math.floor(gauss(4.5, 1, 21));
    }

    public static int g40() {
        return (int) Math.floor(gauss(9.01, 1, 41));
    }

    public static int g40_plus() {
        return (int) Math.floor(gauss(9.01, 3, 41));
    }

    public static int g40_minus() {
        return (int) Math.floor(gauss(9.01, 1, 39));
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
}
