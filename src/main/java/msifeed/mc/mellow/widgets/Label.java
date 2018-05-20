package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

public class Label extends Widget {
    public String text = "";
    public int color = Mellow.THEME.colors.get("text");

    public Label(Widget parent) {
        super(parent);
    }

    @Override
    public void renderSelf() {
//        GL11.glPushMatrix();
//        GL11.glScalef(0.8f, 0.8f, 1f);
        final Point3f partenPos = getParent().pos;
        final int x = MathHelper.floor_float(partenPos.x + pos.x);
        final int y = MathHelper.floor_float(partenPos.y + pos.y);
        Minecraft.getMinecraft().fontRenderer.drawString(text, x, y, color);
//        GL11.glPopMatrix();
    }

    @Override
    public void renderChildren() {
    }
}
