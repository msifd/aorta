package msifeed.mc.mellow.render;

import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public final class RenderWidgets {
    public static void cropped(Widget widget, Rect bounds) {
        final Minecraft mc = Minecraft.getMinecraft();
        final int sf = RenderUtils.getScreenScaleFactor();
        final int x = bounds.x * sf;
        final int y = bounds.y * sf;
        final int w = bounds.w * sf;
        final int h = bounds.h * sf;
        GL11.glScissor(x, mc.displayHeight - h - y, w, h);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        widget.render();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void string(Rect rect, String text, int color) {
        final Minecraft mc = Minecraft.getMinecraft();
        mc.fontRenderer.drawString(text, rect.x, rect.y + 1, color); // +1 tuning
    }
}
