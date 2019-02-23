package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class AnchorLayout implements Layout {
    private Anchor horAnchor;
    private Anchor verAnchor;

    public AnchorLayout() {
        this.horAnchor = this.verAnchor = Anchor.CENTER;
    }

    public AnchorLayout(Anchor hor, Anchor ver) {
        this.horAnchor = hor;
        this.verAnchor = ver;
    }

    @Override
    public Point layoutIndependent(Widget parent, Collection<Widget> children) {
        if (children.isEmpty())
            return new Point();

        final Point topLeft = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        final Point bottomRight = new Point(0, 0);

        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.reset();
            childGeom.setSize(LayoutUtils.getPreferredSize(child));
            childGeom.translate(child.getPos(), child.getZLevel());
            child.setDirty();

            if (childGeom.x < topLeft.x)
                topLeft.x = childGeom.x;
            if (childGeom.y < topLeft.y)
                topLeft.y = childGeom.y;

            if (childGeom.x + childGeom.w > bottomRight.x)
                bottomRight.x = childGeom.x + childGeom.w;
            if (childGeom.y + childGeom.h > bottomRight.y)
                bottomRight.y = childGeom.y + childGeom.h;
        }

        final Point contentSize = new Point(bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
        final Margins margin = parent.getMargin();
        contentSize.translate(margin.horizontal(), margin.vertical());

        return contentSize;
    }

    @Override
    public void layoutRelativeParent(Widget parent, Collection<Widget> children) {
        if (children.isEmpty())
            return;

        final Geom geometry = LayoutUtils.getGeomWithMargin(parent);

        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();

            switch (horAnchor) {
                case CENTER:
                    childGeom.x += (geometry.w - childGeom.w) / 2;
                    break;
                case RIGHT:
                    childGeom.x += geometry.w - childGeom.w;
                    break;
            }
            switch (verAnchor) {
                case CENTER:
                    childGeom.y += (geometry.h - childGeom.h) / 2;
                    break;
            }
            childGeom.translate(geometry);
        }
    }

    public enum Anchor {
        TOP, BOTTOM, LEFT, RIGHT, CENTER
    }
}
