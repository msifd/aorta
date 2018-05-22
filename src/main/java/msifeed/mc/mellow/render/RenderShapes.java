package msifeed.mc.mellow.render;

import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

public final class RenderShapes {
    public static void frame(Point3f pos, Point2f size) {
        final float w = size.x;
        final float h = size.y;
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(2);
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL11.GL_LINE_LOOP);
        tessellator.setColorRGBA_I(0xff0000, 255);
        tessellator.addVertex(pos.x + 0, pos.y + h, pos.z);
        tessellator.addVertex(pos.x + w, pos.y + h, pos.z);
        tessellator.addVertex(pos.x + w, pos.y + 0, pos.z);
        tessellator.addVertex(pos.x + 0, pos.y + 0, pos.z);
        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
}
