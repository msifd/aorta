package msifeed.mc.aorta.utils;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class L10n {
    public static String string(String key) {
        return LanguageRegistry.instance().getStringLocalization(key);
    }

    public static String format(String key, Object... args) {
        return String.format(LanguageRegistry.instance().getStringLocalization(key), args);
    }
}
