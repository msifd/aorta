package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.widgets.Widget;

import java.util.ArrayList;
import java.util.Collection;

public class FloatLayout extends Layout {
    public static final FloatLayout INSTANCE = new FloatLayout();

    private FloatLayout() {
    }

    @Override
    public void apply(Widget widget, Collection<Widget> children) {
        final Margins margin = widget.getMargin();
        final Rect geometry = new Rect(widget.getGeometry());
        geometry.offsetPos(margin);
        geometry.offsetSize(margin);

        for (Widget child : children) {
            final Rect itemGeom = new Rect();
            itemGeom.translate(geometry);
            itemGeom.translate(child.getPos());
            itemGeom.setSize(child.getSizeHint());
            child.setGeometry(itemGeom);
            child.update();
        }
    }
}
