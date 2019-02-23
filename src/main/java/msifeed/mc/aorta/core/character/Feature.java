package msifeed.mc.aorta.core.character;

import msifeed.mc.aorta.utils.L10n;

public enum Feature {
    STR, DEX, END, INT;

    public String tr() {
        return L10n.tr("aorta.feature." + name().toLowerCase());
    }
}
