package msifeed.mc.mellow;

import msifeed.mc.mellow.theme.Theme;
import net.minecraft.util.ResourceLocation;

public class Mellow {
    public static Theme THEME = null;

    public static void loadTheme(ResourceLocation sprite, String metaJson) {
        THEME = Theme.load(sprite, metaJson);
    }
}
