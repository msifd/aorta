package msifeed.mc.mellow.widgets.spoiler;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.WrapWidget;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public class Spoiler extends Widget {
    private final int borderColor = Mellow.getColor("dark_border");

    private final SpoilerButton spoilerButton;
    private final Widget contentWrap;

    public Spoiler(String label, Widget content) {
        setLayout(new ListLayout(ListLayout.Direction.VERTICAL, 0));

        contentWrap = new Container(content);
        contentWrap.setVisible(false);
        contentWrap.getMargin().set(0, 0, 2, 0);

        spoilerButton = new SpoilerButton(label);
        spoilerButton.setClickCallback(() -> {
            spoilerButton.open = !spoilerButton.open;
            contentWrap.setVisible(spoilerButton.open);
        });

        addChild(spoilerButton);
        addChild(contentWrap);
    }

    @Override
    protected void postRenderSelf() {
        if (!contentWrap.isVisible())
            return;

        final int thickness = 2;
        final Geom wrap = contentWrap.getGeometry();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(thickness);
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL11.GL_LINES);
        tessellator.setColorRGBA_I(borderColor, 255);
        tessellator.addVertex(wrap.x + .3, wrap.y, wrap.z);
        tessellator.addVertex(wrap.x + .3, wrap.y + wrap.h - .3, wrap.z);

        tessellator.addVertex(wrap.x, wrap.y + wrap.h - .3, wrap.z);
        tessellator.addVertex(wrap.x + wrap.w, wrap.y + wrap.h - .3, wrap.z);

        tessellator.addVertex(wrap.x + wrap.w - .3, wrap.y + wrap.h, wrap.z);
        tessellator.addVertex(wrap.x + wrap.w - .3, wrap.y, wrap.z);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private class Container extends WrapWidget {
        public Container(Widget content) {
            super(content);
        }

        @Override
        public Point getContentSize() {
            return spoilerButton.open ? super.getContentSize() : new Point();
        }
    }
}
