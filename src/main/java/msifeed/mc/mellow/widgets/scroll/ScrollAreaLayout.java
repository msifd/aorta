package msifeed.mc.mellow.widgets.scroll;

import msifeed.mc.mellow.layout.Layout;
import msifeed.mc.mellow.layout.LayoutUtils;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraft.util.MathHelper;

import java.util.Collection;

public class ScrollAreaLayout implements Layout {
    private int spacing = 1;

    @Override
    public Point layoutIndependent(Widget parent, Collection<Widget> children) {
        final Point contentSize = new Point();
        if (!(parent instanceof ScrollArea))
            return contentSize;

        final ScrollArea scroll = (ScrollArea) parent;
        final Point scrollSize = scroll.getSizeHint();

        final ScrollAreaThumb thumb = scroll.thumb;
        final Geom thumbGeom = thumb.getGeometry();
        thumbGeom.reset();
        thumbGeom.setSize(thumb.getSizeHint());
        thumbGeom.translate(thumb.getPos(), thumb.getZLevel());

        final int availableWidth = scrollSize.x - thumbGeom.w - 1;
        contentSize.x = availableWidth;
        thumbGeom.translate(availableWidth + 1, 0);

        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.reset();
            childGeom.setSize(LayoutUtils.getPreferredSize(availableWidth, child.getContentSize().y, child));
            childGeom.translate(child.getPos(), child.getZLevel());
            childGeom.translate(0, contentSize.y);
            childGeom.translate(0, spacing);

            contentSize.y += childGeom.h;
        }

        final int scrollHeight = contentSize.y - scrollSize.y;
        final double percent;

        if (scrollHeight > 0) {
            thumb.setVisible(true);
            final double scrollWindowPercent = (double) scrollSize.y / contentSize.y;
            thumbGeom.h = MathHelper.floor_double(scrollSize.y * scrollWindowPercent);

            percent = thumb.getPos().y / (double) (scrollSize.y - thumbGeom.h);
        } else {
            thumb.setVisible(false);
            percent = 0;
        }

        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.translate(0, -MathHelper.floor_double(scrollHeight * percent));
        }

        return contentSize;
    }

    @Override
    public void layoutRelativeParent(Widget parent, Collection<Widget> children) {
        if (!(parent instanceof ScrollArea))
            return;

        final ScrollArea scroll = (ScrollArea) parent;
        final ScrollAreaThumb thumb = scroll.thumb;

        final Geom geometry = LayoutUtils.getGeomWithMargin(parent);

        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.translate(geometry);
        }

        final Geom thumbGeom = thumb.getGeometry();
        thumbGeom.translate(geometry);
    }
}
