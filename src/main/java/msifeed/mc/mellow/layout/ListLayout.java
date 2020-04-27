package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class ListLayout implements Layout {
    public static final ListLayout VERTICAL = new ListLayout(Direction.VERTICAL, 1);
    public static final ListLayout HORIZONTAL = new ListLayout(Direction.HORIZONTAL, 1);

    private final int spacing;
    private final Direction direction;
    public SecondaryFillPolicy fillPolicy = SecondaryFillPolicy.EXTEND;

    public ListLayout(Direction direction, int spacing) {
        this.direction = direction;
        this.spacing = spacing;
    }

    @Override
    public Point layoutIndependent(Widget parent, Collection<Widget> children) {
        if (children.isEmpty())
            return new Point();

        int offsetPrimary = 0;
        int maxSecondary = 0;

        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.reset();
            childGeom.setSize(LayoutUtils.getPreferredSize(child));
            childGeom.translate(child.getPos(), child.getZLevel());

            if (direction == Direction.VERTICAL) {
                childGeom.y += offsetPrimary;
                offsetPrimary += childGeom.h + spacing;
                maxSecondary = Math.max(maxSecondary, childGeom.w);
            } else {
                childGeom.x += offsetPrimary;
                offsetPrimary += childGeom.w + spacing;
                maxSecondary = Math.max(maxSecondary, childGeom.h);
            }
        }

        final Point contentSize;
        if (direction == Direction.VERTICAL)
            contentSize = new Point(maxSecondary, offsetPrimary - spacing);
        else
            contentSize = new Point(offsetPrimary - spacing, maxSecondary);
        final Margins margin = parent.getMargin();
        contentSize.translate(margin.horizontal(), margin.vertical());

        return contentSize;
    }

    @Override
    public void layoutRelativeParent(Widget parent, Collection<Widget> children) {
        final Geom geometry = parent.getGeomWithMargin();

        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.translate(geometry);
            switch (fillPolicy) {
                case EXTEND:
                    if (direction == Direction.VERTICAL)
                        childGeom.setSize(LayoutUtils.getPreferredSize(geometry.w, childGeom.h, child));
                    else
                        childGeom.setSize(LayoutUtils.getPreferredSize(childGeom.w, geometry.h, child));
                    break;
                case CENTER:
                    // TODO:
                    break;
            }
        }
    }

    public enum Direction {
        VERTICAL, HORIZONTAL
    }

    public enum SecondaryFillPolicy {
        EXTEND, CENTER
    }
}
