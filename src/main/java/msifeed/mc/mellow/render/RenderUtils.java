package msifeed.mc.mellow.render;

import net.minecraft.client.Minecraft;

public final class RenderUtils {
    private static int cacheSizeHint = 0;
    private static int cacheScaleFactor = 0;
    
    public static int getScreenScaleFactor() {
        final Minecraft mc = Minecraft.getMinecraft();
        
        final int sizeHint = mc.displayWidth + mc.displayHeight + (mc.func_152349_b() ? 1 : 2);
        if (cacheSizeHint == sizeHint)
            return cacheScaleFactor;

        final boolean unicode = mc.func_152349_b();
        final int width = mc.displayWidth;
        final int height = mc.displayHeight;
        int guiScale = mc.gameSettings.guiScale;
        int scaleFactor = 1;

        if (guiScale == 0)
            guiScale = 1000;

        while (scaleFactor < guiScale && width / (scaleFactor + 1) >= 320 && height / (scaleFactor + 1) >= 240)
            ++scaleFactor;

        if (unicode && scaleFactor % 2 != 0 && scaleFactor != 1)
            --scaleFactor;

        cacheSizeHint = sizeHint;
        cacheScaleFactor = scaleFactor;

        return cacheScaleFactor;
    }
}
