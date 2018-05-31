package msifeed.mc.mellow.handlers;

import msifeed.mc.mellow.utils.Offset;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

public class DragHandler {
    protected final Widget target;

    protected boolean dragging = false;
    protected Point dragStart = new Point();
    protected Offset marginOnStart = new Offset();

    public DragHandler(Widget w) {
        target = w;
    }

    public void startDrag(Point p) {
        dragging = true;
        dragStart.set(p);
        marginOnStart.set(target.getMargin());
    }

    public void drag(Point p) {
        if (dragging) {
            final Offset m = target.getMargin();
            m.left = marginOnStart.left + p.x - dragStart.x;
            m.top = marginOnStart.top + p.y - dragStart.y;
            target.markDirty();
        }
    }

    public void stopDrag() {
        dragging = false;
    }
}
