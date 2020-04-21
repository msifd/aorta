package msifeed.mc.extensions.chat;

public class ChatDefines {
    public int offtopRadius = 15;
    public int rollRadius = 15;
    public int logRadius = 15;
    public int[] speechRadius = {2, 5, 15, 30, 60};
    public GarbleSettings garble = new GarbleSettings();

    public static class GarbleSettings {
        public int thresholdDistance = 4;
        public float grayThreshold = 0.33f;
        public float darkGrayThreshold = 0.66f;
        public float missThreshold = 0.9f;
    }
}
