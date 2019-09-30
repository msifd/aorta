package msifeed.mc.mellow.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public final class RenderUtils {
    private static int cacheHint = 0;
    private static ScaledResolution scaledResolution = null;

    public static ScaledResolution getScaledResolution() {
        final Minecraft mc = Minecraft.getMinecraft();

        final int hint = mc.displayWidth + mc.displayHeight + (mc.func_152349_b() ? 1 : 2) + mc.gameSettings.guiScale;
        if (scaledResolution == null || cacheHint != hint) {
            scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            cacheHint = hint;
        }

        return scaledResolution;
    }
}
