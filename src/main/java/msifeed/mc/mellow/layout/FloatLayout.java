package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class FloatLayout extends Layout {
    public static final FloatLayout INSTANCE = new FloatLayout();

    private FloatLayout() {
    }

    @Override
    public void apply(Widget widget, Collection<Widget> children) {
        final Margins margin = widget.getMargin();
        final Geom geometry = new Geom(widget.getGeometry());
        geometry.offsetPos(margin);
        geometry.offsetSize(margin);

        for (Widget child : children) {
            final Geom itemGeom = child.getGeometry();
            itemGeom.set(geometry);
            itemGeom.setSize(child.getLayoutSizeHint());
            itemGeom.translate(child.getPos(), child.getZLevel());
            child.setDirty();
        }
    }
}
