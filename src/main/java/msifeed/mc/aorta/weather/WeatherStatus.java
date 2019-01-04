package msifeed.mc.aorta.weather;

public class WeatherStatus {
    public float snowfall = 0;
    public boolean winter = false;

    public boolean isSnowing() {
        return snowfall > 0;
    }
}
