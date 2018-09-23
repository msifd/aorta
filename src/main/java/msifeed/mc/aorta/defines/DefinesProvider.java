package msifeed.mc.aorta.defines;

import msifeed.mc.aorta.defines.data.AortaDefines;
import msifeed.mc.aorta.defines.data.HealthDefines;

public class DefinesProvider {
    public static AortaDefines load() {
        return defaults();
    }

    private static AortaDefines defaults() {
        final AortaDefines defines = new AortaDefines();
        defines.health = new HealthDefines();
        return defines;
    }
}
