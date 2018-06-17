package msifeed.mc.aorta.core.character;

public enum Feature {
    TRAINING, DEXTERITY, TENACITY, INTELLIGENCE;

    @Override
    public String toString() {
        switch (this) {
            case TRAINING:
                return "Training";
            case DEXTERITY:
                return "Dexterity";
            case TENACITY:
                return "Tenacity";
            case INTELLIGENCE:
                return "Intelligence";
            default:
                return super.toString();
        }
    }
}
