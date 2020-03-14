package msifeed.mc.mellow.widgets.text;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;

import java.util.Collections;
import java.util.List;

public class WordwrapLabel extends Widget {
    private int brightColor = Mellow.getColor("text_bright");
    private int darkColor = Mellow.getColor("text_dark");

    private List<String> lines = Collections.emptyList();
    private int color = brightColor;

    public WordwrapLabel() {
        setZLevel(1);
        setSizePolicy(SizePolicy.FIXED);
        setSizeHint(10, 10);
    }

    public List<String> getLines() {
        return lines;
    }

    public void setText(String text) {
        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        lines = fr.listFormattedStringToWidth(text, getSizeHint().x);
        getSizeHint().y = lines.size() * fr.FONT_HEIGHT;
    }

    @Override
    protected void renderSelf() {
        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        final Geom geom = getGeometry();

        int yOffset = 0;

        for (String line : lines) {
            RenderWidgets.string(line, geom.x, geom.y + yOffset, geom.z, color, fr);
            yOffset += fr.FONT_HEIGHT;
        }
    }
}
