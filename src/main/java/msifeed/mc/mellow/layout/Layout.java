package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public interface Layout {
    Point layoutIndependent(Widget parent, Collection<Widget> children);

    default void layoutRelativeParent(Widget parent, Collection<Widget> children) {
        final Geom geometry = LayoutUtils.getGeomWithMargin(parent);
        for (Widget w : children)
            w.getGeometry().translate(geometry.x, geometry.y, geometry.z);
    }
}
