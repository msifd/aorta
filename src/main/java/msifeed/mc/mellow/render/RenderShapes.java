package msifeed.mc.mellow.render;

import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

public final class RenderShapes {
    public static void frame(Point3f pos, Point2f size, float width, int color) {
        final float w = size.x;
        final float h = size.y;
//        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(width);
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL11.GL_LINE_LOOP);
        tessellator.setColorRGBA_I(color, 255);
        tessellator.addVertex(pos.x + 0, pos.y + h, pos.z);
        tessellator.addVertex(pos.x + w, pos.y + h, pos.z);
        tessellator.addVertex(pos.x + w, pos.y + 0, pos.z);
        tessellator.addVertex(pos.x + .3, pos.y + 0, pos.z); // .3 to fix corner pixel
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        GL11.glPopMatrix();
    }

    public static void rect(Point3f pos, Point2f size, int color, int alpha) {
        final float w = size.x;
        final float h = size.y;
//        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL11.GL_QUADS);
        tessellator.setColorRGBA_I(color, alpha);
        tessellator.addVertex(pos.x + 0, pos.y + h, pos.z);
        tessellator.addVertex(pos.x + w, pos.y + h, pos.z);
        tessellator.addVertex(pos.x + w, pos.y + 0, pos.z);
        tessellator.addVertex(pos.x + 0, pos.y + 0, pos.z);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        GL11.glPopMatrix();
    }
}
