package msifeed.mc.mellow.widgets.text;

import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;

import java.util.List;
import java.util.stream.Stream;

public abstract class TextWall extends Widget {
    public abstract int getColor();

    public abstract int getLineSkip();
    public abstract void setLineSkip(int skip);
    public abstract void setLineLimit(int limit);

    public abstract int getLineCount();
    public abstract Stream<String> getLines();

    public void setLines(List<String> lines) {
        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        int maxWidth = 0;
        for (String l : lines) {
            final int w = fr.getStringWidth(l);
            if (w > maxWidth)
                maxWidth = w;
        }
        setSizeHint(maxWidth, lines.size() * lineHeight());
    }

    @Override
    protected void renderSelf() {
        final Geom geom = new Geom(getGeometry());
        final int color = getColor();
        final int lineHeight = lineHeight();
        getLines()
                .forEach(line -> {
                    RenderWidgets.string(geom, line, color);
                    geom.y += lineHeight;
                });
    }

    public int lineHeight() {
        return RenderManager.instance.getFontRenderer().FONT_HEIGHT - 2;
    }
}
