package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;

public class WidgetItem extends LayoutItem {
    private final Widget widget;

    public WidgetItem(Widget w) {
        this.widget = w;
    }

    @Override
    public Point getPos() {
        return widget.getPos();
    }

    @Override
    public Point getSizeHint() {
        return widget.getSizeHint();
    }

    @Override
    public SizePolicy getSizePolicy() {
        return widget.getSizePolicy();
    }

    @Override
    public Rect getGeometry() {
        return widget.getGeometry();
    }

    @Override
    public void setGeometry(Rect rect) {
        widget.setGeometry(rect);
    }

    @Override
    public void update() {
        widget.update();
    }
}
