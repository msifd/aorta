package msifeed.mc.mellow.render;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.theme.Part;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

public final class RenderParts {
    public static void patch(ResourceLocation tex, double x, double y, double z, double u, double v, double w, double h) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
        patch(x, y, z, u, v, w, h, w, h);
    }

    public static void patch(double x, double y, double z, double w, double h, double u, double v, double tw, double th) {
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

    public static void ninePatches(Part part, Point3f pos, Point2f size) {
        if (part == null || pos == null || size == null || part.patchesSize == null)
            return;

        Minecraft.getMinecraft().getTextureManager().bindTexture(Mellow.THEME.sprite);

        double x = pos.x;
        double y = pos.y;
        for (int i = 0; i < 9; i++) {
            final int xth = i % 3;
            final int yth = i / 3;

            final Point2f patchUV = part.patchesUV[i];
            final Point2f patchSize = part.patchesSize[i];
            final double width = xth == 1 ? Math.max(size.x, patchSize.x) : patchSize.x;
            final double height = yth == 1 ? Math.max(size.y, patchSize.y) : patchSize.y;

            patch(x, y, pos.z, width, height, patchUV.x, patchUV.y, patchSize.x, patchSize.y);

            if (xth == 2) {
                x = pos.x;
                y += height;
            } else {
                x += width;
            }
        }
    }
}
