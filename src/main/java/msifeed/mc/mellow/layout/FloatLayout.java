package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Offset;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.widgets.Widget;

public class FloatLayout extends Layout {
    @Override
    public void update() {
        final Offset hPadding = host.getPadding();
        for (Widget w : host.getChildren()) {
            final Point minSize = w.getMinSize();
            final Offset margin = w.getMargin();
            final Rect bounds = w.getBounds();
            bounds.setPos(margin.left, margin.top, minSize.x, minSize.y);
            bounds.translate(host.getBounds());
            bounds.translate(hPadding.left, hPadding.top);
        }
    }
}
