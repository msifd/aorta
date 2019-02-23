package msifeed.mc.aorta.core.rolls;

public abstract class Roll {
    public Modifiers mods;
    public int sanity;
    public int roll;
    public int result;
    public Critical critical;

    static int sanityMod(int sanity) {
        if (sanity > 100)
            return 1;
        else if (sanity > 75)
            return 0;
        else if (sanity > 50)
            return -1;
        else if (sanity > 25)
            return -2;
        else if (sanity > 1)
            return -3;
        else
            return -999;
    }
}
