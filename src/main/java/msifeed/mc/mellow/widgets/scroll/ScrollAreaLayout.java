package msifeed.mc.mellow.widgets.scroll;

import msifeed.mc.mellow.layout.Layout;
import msifeed.mc.mellow.layout.LayoutUtils;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class ScrollAreaLayout implements Layout {
    private int spacing = 1;

    @Override
    public Point layoutIndependent(Widget parent, Collection<Widget> children) {
        final Point contentSize = new Point();

        if (!(parent instanceof ScrollArea))
            return contentSize;

        final ScrollArea scroll = (ScrollArea) parent;
        final Widget content = scroll.content;
        final ScrollAreaThumb thumb = scroll.thumb;

        final Geom cGeom = content.getGeometry();
        cGeom.reset();
        cGeom.setSize(LayoutUtils.getPreferredSize(content));
        cGeom.translate(content.getPos(), content.getZLevel());

        final Geom tGeom = thumb.getGeometry();
        tGeom.reset();
        tGeom.setSize(LayoutUtils.getPreferredSize(thumb));
        tGeom.translate(thumb.getPos(), thumb.getZLevel());
        tGeom.translate(cGeom.w + spacing, 0);

        contentSize.translate(cGeom.w, cGeom.h);
        contentSize.translate(tGeom.w + spacing, 0);
        final Margins margin = parent.getMargin();
        contentSize.translate(margin.horizontal(), margin.vertical());

        return contentSize;
    }

    @Override
    public void layoutRelativeParent(Widget parent, Collection<Widget> children) {
        if (!(parent instanceof ScrollArea))
            return;

        final ScrollArea scroll = (ScrollArea) parent;
        final Widget content = scroll.content;
        final ScrollAreaThumb thumb = scroll.thumb;

        final Geom geometry = LayoutUtils.getGeomWithMargin(parent);
        final Geom cGeom = content.getGeometry();
        final Geom tGeom = thumb.getGeometry();

        // content geometry is larger than wrapper
        final double contentRelativeSize = geometry.h / (double) cGeom.h;
        final int maxScrollHeight = cGeom.h - geometry.h;
        final int scrolledHeight = (int) (scroll.scroll * maxScrollHeight);
        final int thumbScrolledHeight = (int) (scrolledHeight * contentRelativeSize);

        cGeom.setPos(0, -scrolledHeight);
        tGeom.setPos(cGeom.w + spacing, thumbScrolledHeight);
        tGeom.translate(thumb.getPos());

        for (Widget w : children)
            w.getGeometry().translate(geometry);
    }
}
