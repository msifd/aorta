package msifeed.mc.more.client.gui.status;

import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.widgets.text.Label;

import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;

public class WikiUrlLabel extends Label implements MouseHandler.Click {
    public WikiUrlLabel() {
    }

    public WikiUrlLabel(String text) {
        super(text);
    }

    @Override
    protected int getColor() {
        if (isHovered())
            return Color.CYAN.darker().getRGB();
        return Color.CYAN.getRGB();
    }

    @Override
    protected void renderSelf() {
        super.renderSelf();

        final Geom geom = getGeometry();
        final Geom g = new Geom(geom);
        g.x -= 1;
        g.y += geom.h;
        g.w += 1;
        g.h = 0;
        RenderShapes.line(g, 1, getColor());
    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {
        if (Desktop.isDesktopSupported()) {
            try {
                final String url = "https://wiki.ortega.su/index.php?search=" + URLEncoder.encode(getText(), "UTF-8");
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
            }
        }
    }
}
