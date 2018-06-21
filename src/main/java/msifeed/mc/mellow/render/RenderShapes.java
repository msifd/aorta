package msifeed.mc.mellow.render;

import msifeed.mc.mellow.utils.Geom;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public final class RenderShapes {
    public static void frame(Geom geom, float width, int color) {
        final float w = geom.w;
        final float h = geom.h;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(width);
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL11.GL_LINE_LOOP);
        tessellator.setColorRGBA_I(color, 255);
        tessellator.addVertex(geom.x + 0, geom.y + h, 0);
        tessellator.addVertex(geom.x + w, geom.y + h, 0);
        tessellator.addVertex(geom.x + w, geom.y + 0, 0);
        tessellator.addVertex(geom.x + .3, geom.y + 0, 0); // .3 to fix corner pixel
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void rect(Geom bounds, int color, int alpha) {
        final float w = bounds.w;
        final float h = bounds.h;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL11.GL_QUADS);
        tessellator.setColorRGBA_I(color, alpha);
        tessellator.addVertex(bounds.x + 0, bounds.y + h, 0);
        tessellator.addVertex(bounds.x + w, bounds.y + h, 0);
        tessellator.addVertex(bounds.x + w, bounds.y + 0, 0);
        tessellator.addVertex(bounds.x + 0, bounds.y + 0, 0);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
