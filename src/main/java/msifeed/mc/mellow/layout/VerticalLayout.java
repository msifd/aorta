package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
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
    public Point layoutIndependent(Collection<Widget> children) {
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

        return new Point(maxWidth, yOffset - spacing);
    }

    @Override
    public void layoutRelativeParent(Widget parent, Collection<Widget> children) {
        final Geom geometry = LayoutUtils.getGeomWithMargin(parent);

        final int spacingsHeight = spacing * (children.size() - 1);
        final int averageHeight = (geometry.h - spacingsHeight) / children.size();
        int fixedHeight = spacingsHeight;
        int fixedChildren = 0;
        for (Widget child : children) {
            final int h = LayoutUtils.getPreferredHeight(averageHeight, child);
            if (h != averageHeight) {
                fixedHeight += h;
                fixedChildren++;
            }
        }

        final int freeChildren = Math.max(1, children.size() - fixedChildren);
        final int targetChildHeight = (geometry.h - fixedHeight) / freeChildren;

        int yOffset = geometry.y;
        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.setPos(0, 0);
            childGeom.translate(geometry.x, yOffset, geometry.z);
            childGeom.setSize(LayoutUtils.getPreferredWidth(geometry.w, child), LayoutUtils.getPreferredHeight(targetChildHeight, child));

            yOffset += childGeom.h + spacing;
        }
    }
}
