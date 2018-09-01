package msifeed.mc.aorta.core.defines;

public class DefinesProvider {
    public static CoreDefines load() {
        return defaults();
    }

    private static CoreDefines defaults() {
        final CoreDefines core = new CoreDefines();
        core.health = new HealthDefines();
        return core;
    }
}
