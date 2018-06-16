package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.utils.SizePolicy;
import net.minecraft.client.Minecraft;

public class Label extends Widget {
    protected int color = Mellow.THEME.colors.get("text");
    protected String text = "";

    public Label() {
        this("");
    }

    public Label(String text) {
        setText(text);
        setSizePolicy(SizePolicy.Policy.PREFERRED, SizePolicy.Policy.MAXIMUM);
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
