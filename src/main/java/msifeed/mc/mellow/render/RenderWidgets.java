package msifeed.mc.mellow.render;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;

public final class RenderWidgets {
    private static boolean croppingActive = false;
    private static boolean croppingPaused = false;

    public static void beginCropped(Geom geom) {
        final Minecraft mc = Minecraft.getMinecraft();
        final int sf = RenderUtils.getScreenScaleFactor();
        final int x = geom.x * sf;
        final int y = geom.y * sf;
        final int w = geom.w * sf;
        final int h = geom.h * sf;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x, mc.displayHeight - h - y, w, h);
//        RenderShapes.rect(new Geom(x, mc.displayHeight - h - y, w, h), 0, 0);
        croppingActive = true;
    }

    public static void endCropped() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        croppingActive = false;
    }

    public static void toggleCropping() {
        if (!croppingActive)
            return;
        if (croppingPaused)
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
        else
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        croppingPaused = !croppingPaused;
    }

    public static void cropped(Widget widget, Geom geom) {
        beginCropped(geom);
        widget.render();
        endCropped();
    }

    public static void string(Geom geom, String text, int color) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, geom.z);
        RenderManager.instance.getFontRenderer().drawString(text, geom.x, geom.y + 1, color); // +1 tuning
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPopMatrix();
    }
}
