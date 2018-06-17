package msifeed.mc.mellow.render;

import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public final class RenderWidgets {
    public static void beginCropped(Widget widget, Rect geom) {
        final Minecraft mc = Minecraft.getMinecraft();
        final int sf = RenderUtils.getScreenScaleFactor();
        final int x = geom.x * sf;
        final int y = geom.y * sf;
        final int w = geom.w * sf;
        final int h = geom.h * sf;
        GL11.glScissor(x, mc.displayHeight - h - y, w, h);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    public static void endCropped() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void cropped(Widget widget, Rect geom) {
        beginCropped(widget, geom);
        widget.render();
        endCropped();
    }

    public static void string(Rect geom, String text, int color) {
        final Minecraft mc = Minecraft.getMinecraft();
        mc.fontRenderer.drawString(text, geom.x, geom.y + 1, color); // +1 tuning
    }
}
