package msifeed.mc.mellow.widgets.basic;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraft.client.Minecraft;

public class Label extends Widget {
    public int brightColor = Mellow.THEME.colors.get("text_bright");
    public int darkColor = Mellow.THEME.colors.get("text_dark");

    private String text = "";
    private int color = brightColor;

    public Label() {
        this("");
    }

    public Label(String text) {
        setText(text);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        final Minecraft mc = Minecraft.getMinecraft();
        this.text = text;
        setSizeHint(mc.fontRenderer.getStringWidth(text), mc.fontRenderer.FONT_HEIGHT);
    }

    protected int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    protected void renderSelf() {
        RenderWidgets.string(getGeometry(), text, color);
    }

}
