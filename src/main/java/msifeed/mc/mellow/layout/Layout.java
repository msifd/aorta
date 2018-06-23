package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public abstract class Layout {
    public Point getSizeOfContent(Widget widget) {
        return widget.getSizeHint();
    }

    public abstract void apply(Widget widget, Collection<Widget> children);

    protected static int getPreferredSize(int target, int hint, SizePolicy.Policy policy) {
        if (target > hint && policy.canGrow) {
            return target;
        } else if (target < hint && policy.canShrink) {
            return target;
        } else {
            return hint;
        }
    }
}
