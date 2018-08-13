package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class FreeLayout implements Layout {
    public static final FreeLayout INSTANCE = new FreeLayout();

    @Override
    public Point layoutIndependent(Collection<Widget> children) {
        final Point contentSize = new Point();

        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.reset();
            childGeom.setSize(LayoutUtils.getPreferredSize(child));
            childGeom.translate(child.getPos(), child.getZLevel());
            child.setDirty();

            contentSize.translate(childGeom.w, childGeom.h);
        }

        return contentSize;
    }
}
