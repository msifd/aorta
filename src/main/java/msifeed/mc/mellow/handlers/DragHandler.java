package msifeed.mc.mellow.handlers;

import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

public class DragHandler {
    protected final Widget target;

    protected boolean dragging = false;
    protected Point dragStart = new Point();
    protected Point posOnStart = new Point();

    public DragHandler(Widget w) {
        target = w;
    }

    public void startDrag(Point p) {
        dragging = true;
        dragStart.set(p);
        posOnStart.set(target.getPos());
    }

    public void drag(Point p) {
        if (dragging)
            target.setPos(posOnStart.x - dragStart.x + p.x, posOnStart.y - dragStart.y + p.y);
    }

    public void stopDrag() {
        dragging = false;
    }
}
