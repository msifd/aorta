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

    public static int getPreferredWidth(int target, Widget widget) {
        return getPreferred(target, widget, true);
    }

    public static int getPreferredHeight(int target, Widget widget) {
        return getPreferred(target, widget, false);
    }

    private static int getPreferred(int target, Widget widget, boolean width) {
        final SizePolicy sp = widget.getSizePolicy();
        final Point sh = widget.getSizeHint();

        final SizePolicy.Policy policy = width ? sp.horizontalPolicy : sp.verticalPolicy;
        int hint = width ? sh.x : sh.y;
        if (hint == 0) {
            final Point lh = widget.getLayoutSizeHint();
            hint = width ? lh.x : lh.y;
        }

        if (target > hint && policy.canGrow) {
            return target;
        } else if (target < hint && policy.canShrink) {
            return target;
        }

        return hint;
    }

//    public static int getPreferredSize(int target, int hint, SizePolicy.Policy policy) {
//        if (target > hint && policy.canGrow) {
//            return target;
//        } else if (target < hint && policy.canShrink) {
//            return target;
//        } else {
//            return hint;
//        }
//    }
}
