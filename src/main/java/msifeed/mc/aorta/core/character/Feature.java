package msifeed.mc.aorta.core.character;

public enum Feature {
    TRAINING, SKILL, TENACITY, INTELLIGENCE;

    @Override
    public String toString() {
        switch (this) {
            case TRAINING:
                return "Training";
            case SKILL:
                return "Skill";
            case TENACITY:
                return "Tenacity";
            case INTELLIGENCE:
                return "Intelligence";
            default:
                return super.toString();
        }
    }
}
