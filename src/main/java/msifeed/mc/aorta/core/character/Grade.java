package msifeed.mc.aorta.core.character;

public enum Grade {
    BAD, NORMAL, GOOD, GREAT, EXCELLENT;

    @Override
    public String toString() {
        switch (this) {
            case BAD:
                return "Bad";
            case NORMAL:
                return "Normal";
            case GOOD:
                return "Good";
            case GREAT:
                return "Great";
            case EXCELLENT:
                return "Excellent";
            default:
                return super.toString();
        }
    }
}
