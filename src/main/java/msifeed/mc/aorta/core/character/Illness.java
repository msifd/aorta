package msifeed.mc.aorta.core.character;

public class Illness {
    private static final int MASK = 0x3ff; // 10 bits per value

    public int illnessThreshold = 0;
    public int illness = 0;
    public int treatment = 0;
    public boolean thresholdVisible = false;

    public Illness() {}

    public int pack() {
        return ((thresholdVisible ? 1 : 0) << 30)
                | (illnessThreshold & MASK) << 20
                | (illness & MASK) << 10
                | treatment & MASK;
    }

    public void unpack(int i) {
        thresholdVisible = (i >> 30 & 1) == 1;
        illnessThreshold = (i >> 20) & MASK;
        illness = (i >> 10) & MASK;
        treatment = i & MASK;
    }
}
