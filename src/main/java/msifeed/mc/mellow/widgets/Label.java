package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

import javax.vecmath.Point3f;

public class Label extends Widget {
    protected int color = Mellow.THEME.colors.get("text");
    protected String text = "";

    public Label(Widget parent) {
        super(parent);
    }

    public Label(Widget parent, String text) {
        this(parent);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        final Minecraft mc = Minecraft.getMinecraft();
        this.text = text;
        setSize(mc.fontRenderer.getStringWidth(text), mc.fontRenderer.FONT_HEIGHT);
    }

    @Override
    protected void renderSelf() {
        final Minecraft mc = Minecraft.getMinecraft();
//        GL11.glPushMatrix();
//        GL11.glScalef(0.8f, 0.8f, 1f);
        final Point3f rel = getAbsPos();
        mc.fontRenderer.drawString(text, MathHelper.floor_float(rel.x), MathHelper.floor_float(rel.y), color);
//        GL11.glPopMatrix();
    }
}
