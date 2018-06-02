package msifeed.mc.mellow.utils;

public class SizePolicy {
    public static final SizePolicy DEFAULT = new SizePolicy(Policy.PREFERRED);

    public Policy horizontalPolicy = Policy.PREFERRED;
    public Policy verticalPolicy = Policy.PREFERRED;

    public SizePolicy() {
    }

    public SizePolicy(Policy both) {
        this.horizontalPolicy = both;
        this.verticalPolicy = both;
    }

    public enum Policy {
        FIXED(false, false), MINIMUM(false, true), MAXIMUM(true, false), PREFERRED(true, true);

        public boolean canShrink;
        public boolean canGrow;

        Policy(boolean s, boolean g) {
            this.canShrink = s;
            this.canGrow = g;
        }
    }
}
