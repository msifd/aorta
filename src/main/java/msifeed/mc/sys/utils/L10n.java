package msifeed.mc.sys.utils;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class L10n {
    public static String tr(String key) {
        return LanguageRegistry.instance().getStringLocalization(key);
    }

    public static String fmt(String key, Object... args) {
        final String loc = LanguageRegistry.instance().getStringLocalization(key);
        return String.format(loc.replaceAll("\\\\n", "\n"), args);
    }
}
