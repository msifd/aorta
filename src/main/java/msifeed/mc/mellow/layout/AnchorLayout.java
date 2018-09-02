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

        final Widget child = children.iterator().next();
        final Geom childGeom = child.getGeometry();
        childGeom.reset();
        childGeom.setSize(LayoutUtils.getPreferredSize(child));
        childGeom.translate(child.getPos(), child.getZLevel());
        child.setDirty();

        final Point contentSize = new Point(child.getSizeHint());
        final Margins margin = parent.getMargin();
        contentSize.translate(margin.horizontal(), margin.vertical());

        return contentSize;
    }

    @Override
    public void layoutRelativeParent(Widget parent, Collection<Widget> children) {
        if (children.isEmpty())
            return;

        final Geom geometry = LayoutUtils.getGeomWithMargin(parent);
        final Widget child = children.iterator().next();
        final Geom childGeom = child.getGeometry();

        switch (horAnchor) {
            case CENTER:
                childGeom.x += (geometry.w - childGeom.w) / 2;
                break;
        }
        switch (verAnchor) {
            case CENTER:
                childGeom.y += (geometry.h - childGeom.h) / 2;
                break;
        }

        childGeom.translate(geometry);
    }

    public enum Anchor {
        TOP, BOTTOM, LEFT, RIGHT, CENTER
    }
}
