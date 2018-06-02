package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderWidgets;
import net.minecraft.client.Minecraft;

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
        setSizeHint(mc.fontRenderer.getStringWidth(text), mc.fontRenderer.FONT_HEIGHT);
    }

    @Override
    protected void renderSelf() {
        RenderWidgets.string(getGeometry(), text, color);
    }
}
