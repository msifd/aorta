package msifeed.mc.aorta.core.character;

import msifeed.mc.aorta.sys.utils.L10n;

public enum Feature {
    STR, DEX, TEN, INT;

    public String tr() {
        return L10n.tr("aorta.feature." + name().toLowerCase());
    }

    public String trShort() {
        return L10n.tr("aorta.feature.short." + name().toLowerCase());
    }

    public enum ComplexFeature {
        ATH(STR, DEX), END(STR, TEN), HND(DEX, INT), PSI(TEN, INT);

        public final Feature[] feats;

        ComplexFeature(Feature... feats) {
            this.feats = feats;
        }

        public String tr() {
            return L10n.tr("aorta.feature." + name().toLowerCase());
        }
    }
}
