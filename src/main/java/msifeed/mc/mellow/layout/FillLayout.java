package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class FillLayout implements Layout {
    public static final FillLayout INSTANCE = new FillLayout();

    @Override
    public Point layoutIndependent(Widget parent, Collection<Widget> children) {
        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.reset();
            childGeom.translate(child.getPos(), child.getZLevel());
        }

        return parent.getSizeHint();
    }

    @Override
    public void layoutRelativeParent(Widget parent, Collection<Widget> children) {
        final Geom geometry = parent.getGeomWithMargin();

        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.translate(geometry);
            childGeom.setSize(geometry.w, geometry.h);
        }
    }
}
