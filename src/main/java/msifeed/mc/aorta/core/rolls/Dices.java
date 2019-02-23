package msifeed.mc.aorta.core.rolls;

import java.util.Random;
import java.util.stream.IntStream;

public class Dices {
    private final static Random random = new Random();

    public static int dice(int n) {
        return random.nextInt(n) + 1;
    }

    public static int dice(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static int nDice(int n, int d) {
        return IntStream.range(0, n).map(i -> dice(d)).sum();
    }

    public static boolean chance(int dice) {
        return random.nextInt(dice) == 0;
    }

    public static boolean coin() {
        return chance(2);
    }

    public static int feature(int f) {
        return nDice(5, 9) - 5 + f;
    }

    public static int randRound(double r) {
        if (r % 1 < 0.5) {
            return (int) Math.floor(r);
        } else if (r % 1 > 0.5) {
            return (int) Math.ceil(r);
        } else {
            if (coin())
                return (int) Math.floor(r);
            else
                return (int) Math.ceil(r);
        }
    }
}
