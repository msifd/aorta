package msifeed.mc.mellow.widgets.basic;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;

public class Label extends Widget {
    public int brightColor = Mellow.getColor("text_bright");
    public int darkColor = Mellow.getColor("text_dark");

    private String text = "";
    private int color = brightColor;

    public Label() {
        this("");
    }

    public Label(String text) {
        setZLevel(1);
        setText(text);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        this.text = text;
        setSizeHint(fr.getStringWidth(text), fr.FONT_HEIGHT);
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
