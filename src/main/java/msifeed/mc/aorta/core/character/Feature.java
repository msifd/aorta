package msifeed.mc.aorta.core.character;

import msifeed.mc.aorta.sys.utils.L10n;

public enum Feature {
    STR, DEX, TEN, INT,
    ATH(STR, DEX), END(STR, TEN), HND(DEX, INT), PSI(TEN, INT);

    public final Feature[] feats;

    Feature() {
        this.feats = new Feature[] { this };
    }

    Feature(Feature... feats) {
        this.feats = feats;
    }

    public boolean isMain() {
        return ordinal() <= INT.ordinal();
    }

    public boolean canTarget() {
        return this == PSI;
    }

    public String tr() {
        return L10n.tr("aorta.feature." + name().toLowerCase());
    }

    public String trShort() {
        return L10n.tr("aorta.feature.short." + name().toLowerCase());
    }

    public static Feature[] mainFeatures() {
        return new Feature[] { STR, DEX, TEN, INT };
    }
}
