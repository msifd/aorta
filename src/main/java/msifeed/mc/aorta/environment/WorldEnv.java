package msifeed.mc.aorta.environment;

public class WorldEnv {
    public boolean rain = false;
    public boolean snow = false;
    public boolean stackSnow = false;
    public boolean meltSnow = false;
    public Time time = new Time();

    public static class Time {
        public String mode = "vanilla";
        public int offsetHours = 0;
        public double scale = 1;
        public long fixedTime = 0;
    }
}
