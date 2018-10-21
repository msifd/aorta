package msifeed.mc.aorta.core.rules;

import msifeed.mc.aorta.core.status.CharStatus;

public class SanityMod {
    public static int calc(CharStatus status) {
        final int s = status.sanity;
        if (s > 100)
            return 1;
        else if (s > 75)
            return 0;
        else if (s > 50)
            return -1;
        else if (s > 25)
            return -2;
        else if (s > 1)
            return -3;
        else
            return -999;
    }
}
