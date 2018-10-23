package msifeed.mc.aorta.core.character;

public enum Feature {
    STRENGTH, DEXTERITY, ENDURANCE, INTELLIGENCE;

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
