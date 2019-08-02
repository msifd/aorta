package msifeed.mc.aorta.core.rolls;

import msifeed.mc.aorta.core.character.CharStatus;

public abstract class Roll {
    public Modifiers mods;
    public int statusMod;
    public int roll;
    public int result;
    public Critical critical;

    public Roll(CharStatus s) {
        this.mods = s.modifiers;
        this.statusMod = sanityMod(s.sanity) + s.illness.debuff();
        this.critical = Critical.roll();
    }

    private static int sanityMod(int sanity) {
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
