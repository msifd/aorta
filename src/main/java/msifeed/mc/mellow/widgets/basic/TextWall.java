package msifeed.mc.mellow.widgets.basic;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;

import java.util.Collections;
import java.util.List;

public class TextWall extends Widget {
    public int darkColor = Mellow.THEME.colors.get("text_dark");
    private List<String> lines = Collections.emptyList();
    private int startLine = 0;
    private int maxLines = 10;

    public TextWall() {
        setZLevel(1);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;

        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        int maxWidth = 0;
        for (String l : lines) {
            final int w = fr.getStringWidth(l);
            if (w > maxWidth)
                maxWidth = w;
        }
        setSizeHint(maxWidth, lines.size() * fr.FONT_HEIGHT);
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getMaxLines() {
        return maxLines;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    @Override
    protected void renderSelf() {
        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        Geom geom = new Geom(getGeometry());
        lines.stream()
                .skip(startLine)
                .limit(maxLines)
                .forEach(line -> {
                    RenderWidgets.string(geom, line, darkColor);
                    geom.y += fr.FONT_HEIGHT;
                });
    }
}
