package msifeed.mc.aorta.core.rolls;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.meta.MetaInfo;

public abstract class Roll {
    public Modifiers mods;
    public int statusMod;
    public int roll;
    public int result;
    public Critical critical;

    public Roll(Character character, MetaInfo meta) {
        this.mods = meta.modifiers;
        this.critical = Critical.roll();
        rules(character, meta);
    }

    private void rules(Character character, MetaInfo meta) {
        statusMod += sanityMod(character.sanity);
        statusMod += character.illness.debuff();
    }

    public static int sanityMod(int sanity) {
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
