package msifeed.mc.more.crabs.character;

import msifeed.mc.sys.utils.L10n;

public enum Ability {
    STR, END, PER, REF, DET, INT, WIL, SPR;

    public String trShort() {
        return L10n.fmt("more.ability.short." + name().toLowerCase());
    }
}
