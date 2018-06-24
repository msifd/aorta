package msifeed.mc.mellow.render;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Point;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public final class RenderParts {
    public static void slice(ResourceLocation tex, double x, double y, double z, double u, double v, double w, double h) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
        slice(x, y, z, u, v, w, h, w, h);
    }

    public static void slice(double x, double y, double z, double w, double h, double u, double v, double tw, double th) {
        if (w <= 0 || h <= 0)
            return;

        // 256:256 texture aspect ratio
        final double f = 0.00390625;
        final double f1 = 0.00390625;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + h, z, (u + 0) * f, (v + th) * f1);
        tessellator.addVertexWithUV(x + w, y + h, z, (u + tw) * f, (v + th) * f1);
        tessellator.addVertexWithUV(x + w, y + 0, z, (u + tw) * f, (v + 0) * f1);
        tessellator.addVertexWithUV(x + 0, y + 0, z, (u + 0) * f, (v + 0) * f1);
        tessellator.draw();
    }

    public static void slice(Part part, int x, int y) {
        if (part == null || part.size == null)
            return;

        bindThemeTexture();
        slice(x, y, 0, part.size.x, part.size.y, part.pos.x, part.pos.y, part.size.x, part.size.y);
    }

    public static void nineSlice(Part part, Geom geom) {
        if (part == null || part.slicesSize == null)
            return;

        bindThemeTexture();

        final double midWidth = Math.max(geom.w - part.slicesSize[0].x - part.slicesSize[3].x, 0);
        final double midHeight = Math.max(geom.h - part.slicesSize[0].y - part.slicesSize[6].y, 0);

        double x = geom.x;
        double y = geom.y;
        double z = geom.z;
        for (int i = 0; i < 9; i++) {
            final int xth = i % 3;
            final int yth = i / 3;

            final Point sliceUV = part.slicesUV[i];
            final Point sliceSize = part.slicesSize[i];
            final double width = xth == 1 ? midWidth : sliceSize.x;
            final double height = yth == 1 ? midHeight : sliceSize.y;

            slice(x, y, z, width, height, sliceUV.x, sliceUV.y, sliceSize.x, sliceSize.y);

            if (xth == 2) {
                x = geom.x;
                y += height;
            } else {
                x += width;
            }
        }
    }

    private static void bindThemeTexture() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(Mellow.THEME.sprite);
    }
}
