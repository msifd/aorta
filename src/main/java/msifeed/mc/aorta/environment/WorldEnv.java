package msifeed.mc.aorta.environment;

import java.time.ZoneId;
import java.time.ZoneOffset;

public class WorldEnv {

    public boolean snow = false;
    public boolean stackSnow = false;
    public boolean meltSnow = false;
    public Time time = new Time();
    public Rain rain = new Rain();

    void load(WorldEnvMapData data) {
        if (data == null)
            return;
        rain.accumulated = data.rainAccumulated;
    }

    public static class Time {
        public String mode = "vanilla";
        public ZoneId timezone = ZoneOffset.UTC;
        public double scale = 1;
        public long fixedTime = 0;
    }

    public static class Rain {
        public int income = 0;
        public int outcome = 0;
        public int minThreshold = 10;
        public int maxThreshold = 100;
        public int thunderThreshold = 70;
        public int rainfallDice = 100;

        public transient long accumulated = 0;
    }
}
