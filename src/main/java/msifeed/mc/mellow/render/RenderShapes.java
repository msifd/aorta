package msifeed.mc.mellow.render;

import msifeed.mc.mellow.utils.Geom;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public final class RenderShapes {
    public static void line(Geom geom, float thickness, int color) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(thickness);
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL11.GL_LINES);
        tessellator.setColorRGBA_I(color, 255);
        tessellator.addVertex(geom.x, geom.y + 0.3, geom.z);
        tessellator.addVertex(geom.x + geom.w, geom.y + geom.h + 0.3, geom.z);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void frame(Geom geom, float thickness, int color) {
        final float w = geom.w;
        final float h = geom.h;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(thickness);
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL11.GL_LINE_LOOP);
        tessellator.setColorRGBA_I(color, 255);
        tessellator.addVertex(geom.x + 0, geom.y + h, geom.z);
        tessellator.addVertex(geom.x + w, geom.y + h, geom.z);
        tessellator.addVertex(geom.x + w, geom.y + 0, geom.z);
        tessellator.addVertex(geom.x + .3, geom.y + 0, geom.z); // .3 to fix corner pixel
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void rect(Geom geom, int color, int alpha) {
        final float w = geom.w;
        final float h = geom.h;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL11.GL_QUADS);
        tessellator.setColorRGBA_I(color, alpha);
        tessellator.addVertex(geom.x + 0, geom.y + h, geom.z);
        tessellator.addVertex(geom.x + w, geom.y + h, geom.z);
        tessellator.addVertex(geom.x + w, geom.y + 0, geom.z);
        tessellator.addVertex(geom.x + 0, geom.y + 0, geom.z);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
