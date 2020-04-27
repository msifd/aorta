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

public class MultilineLabel extends Widget {
    public int brightColor = Mellow.getColor("text_bright");
    public int darkColor = Mellow.getColor("text_dark");

    protected List<String> lines = Collections.emptyList();
    protected int lineSkip = 0;
    protected int lineLimit = 10;

    protected int color = darkColor;

    public MultilineLabel() {
        setZLevel(1);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getLineSkip() {
        return lineSkip;
    }

    public void updateLineSkip(int delta) {
        this.lineSkip += delta;
    }

    public void setLineLimit(int lineLimit) {
        this.lineLimit = lineLimit;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;

        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        final int lineHeight = RenderWidgets.lineHeight();

        int maxWidth = 0;
        for (String l : lines) {
            final int w = fr.getStringWidth(l);
            if (w > maxWidth)
                maxWidth = w;
        }

        setSizeHint(maxWidth, lines.size() * lineHeight);
    }

    @Override
    protected void renderSelf() {
        final Geom geom = this.getGeomWithMargin();
        final int color = getColor();
        final int lineHeight = RenderWidgets.lineHeight();

        lines.stream()
                .skip(lineSkip)
                .limit(lineLimit)
                .forEach(line -> {
                    RenderWidgets.string(geom, line, color);
                    geom.y += lineHeight;
                });
    }
}
