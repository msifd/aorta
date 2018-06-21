package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public abstract class Layout {
    public Point sizeHintOfContent(Widget widget) {
        return widget.getSizeHint();
    }

    public abstract void apply(Widget widget, Collection<Widget> children);
}
