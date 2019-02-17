package msifeed.mc.aorta.core.character;

import msifeed.mc.aorta.utils.L10n;

public enum Feature {
    STRENGTH, DEXTERITY, ENDURANCE, INTELLIGENCE;

    public String tr() {
        return L10n.tr("aorta.features." + name().toLowerCase());
    }

    public String shortName() {
        switch (this) {
            case STRENGTH:
                return "STR";
            case DEXTERITY:
                return "DEX";
            case ENDURANCE:
                return "END";
            case INTELLIGENCE:
                return "INT";
            default:
                return super.toString();
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case STRENGTH:
                return "Strength";
            case DEXTERITY:
                return "Dexterity";
            case ENDURANCE:
                return "Endurance";
            case INTELLIGENCE:
                return "Intelligence";
            default:
                return super.toString();
        }
    }
}
