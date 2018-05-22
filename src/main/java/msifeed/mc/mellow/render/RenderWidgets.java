package msifeed.mc.mellow.render;

import msifeed.mc.mellow.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

public final class RenderWidgets {
    public static void cropped(Widget widget, Point3f pos, Point2f size) {
        final Minecraft mc = Minecraft.getMinecraft();
        final ScaledResolution scaled = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        final int sf = scaled.getScaleFactor();
        final int x = MathHelper.floor_float(pos.x) * sf;
        final int y = MathHelper.floor_float(pos.y) * sf;
        final int w = MathHelper.floor_float(size.x) * sf;
        final int h = MathHelper.floor_float(size.y) * sf;
        GL11.glScissor(x, mc.displayHeight - h - y, w, h);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        widget.render();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
