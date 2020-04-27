package msifeed.mc.mellow.widgets.scroll;

import msifeed.mc.mellow.layout.Layout;
import msifeed.mc.mellow.layout.LayoutUtils;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraft.util.MathHelper;

import java.util.Collection;

class ScrollAreaLayout implements Layout {
    @Override
    public Point layoutIndependent(Widget parent, Collection<Widget> children) {
        final Point contentSize = new Point();
        if (!(parent instanceof ScrollArea))
            return contentSize;

        final ScrollArea scroll = (ScrollArea) parent;
        final Point scrollSize = scroll.getSizeHint();
        final int spacing = scroll.spacing;

        final ScrollAreaThumb thumb = scroll.thumb;
        final Geom thumbGeom = thumb.getGeometry();
        thumbGeom.reset();
        thumbGeom.setSize(thumb.getSizeHint());
        thumbGeom.translate(thumb.getPos(), thumb.getZLevel());

        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.reset();
            childGeom.setSize(LayoutUtils.getPreferredSize(child));
            childGeom.translate(child.getPos(), child.getZLevel());
            childGeom.translate(0, contentSize.y);

            contentSize.x = Math.max(contentSize.x, childGeom.w);
            contentSize.y += childGeom.h + spacing;
        }

        return contentSize;
    }

    @Override
    public void layoutRelativeParent(Widget parent, Collection<Widget> children) {
        if (!(parent instanceof ScrollArea))
            return;

        final Geom geometry = parent.getGeomWithMargin();

        final ScrollArea scroll = (ScrollArea) parent;
        final int spacing = scroll.spacing;
        final ScrollAreaThumb thumb = scroll.thumb;
        final Geom thumbGeom = thumb.getGeometry();
        final int thumbAreaWidth = thumbGeom.w + 1;

        thumbGeom.translate(geometry);
        thumbGeom.translate(geometry.w - thumbGeom.w + 1, 0);

        int contentHeight = 0;
        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.setSize(LayoutUtils.getPreferredSize(geometry.w - thumbAreaWidth, child.getContentSize().y, child));
            childGeom.translate(geometry);

            contentHeight += childGeom.h + spacing;
        }

        final int scrollHeight = contentHeight - geometry.h;

        if (scrollHeight > 0) {
            thumb.setVisible(true);
            final double scrollWindowPercent = (double) geometry.h / contentHeight;
            thumbGeom.h = MathHelper.floor_double(geometry.h * scrollWindowPercent);

            final double percent = thumb.getPos().y / (double) (geometry.h - thumbGeom.h);

            for (Widget child : children) {
                final Geom childGeom = child.getGeometry();
                childGeom.translate(0, -MathHelper.floor_double(scrollHeight * percent));
            }
        } else {
            thumb.setVisible(false);
        }
    }
}
