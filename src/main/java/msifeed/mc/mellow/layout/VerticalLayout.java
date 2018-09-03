package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class VerticalLayout implements Layout {
    public static final VerticalLayout INSTANCE = new VerticalLayout(1);

    private final int spacing;

    public VerticalLayout(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public Point layoutIndependent(Widget parent, Collection<Widget> children) {
        int yOffset = 0;
        int maxWidth = 0;

        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.reset();
            childGeom.setSize(LayoutUtils.getPreferredSize(child));
            childGeom.translate(child.getPos(), child.getZLevel());
            childGeom.translate(0, yOffset);
            child.setDirty();

            yOffset += childGeom.h + spacing;
            maxWidth = Math.max(maxWidth, childGeom.w);
        }

        final Point contentSize = new Point(maxWidth, yOffset - spacing);
        final Margins margin = parent.getMargin();
        contentSize.translate(margin.horizontal(), margin.vertical());

        return contentSize;
    }

    @Override
    public void layoutRelativeParent(Widget parent, Collection<Widget> children) {
        final Geom geometry = LayoutUtils.getGeomWithMargin(parent);

        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.translate(geometry);
            childGeom.setSize(LayoutUtils.getPreferredSize(geometry.w, child.getContentSize().y, child));
        }
    }
}
