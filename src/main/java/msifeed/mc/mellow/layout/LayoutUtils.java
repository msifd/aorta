package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;

public class LayoutUtils {

    public static Point getPreferredSize(Widget widget) {
        return getPreferredSize(widget.getContentSize(), widget);
    }

    public static Point getPreferredSize(Point target, Widget widget) {
        return getPreferredSize(target.x, target.y, widget);
    }

    public static Point getPreferredSize(int targetX, int targetY, Widget widget) {
        final Point p = new Point();
        p.x = getPreferredWidth(targetX, widget);
        p.y = getPreferredHeight(targetY, widget);
        return p;
    }

    public static int getPreferredWidth(int target, Widget widget) {
        final int width = widget.getSizeHint().x + widget.getMargin().horizontal();
        return getPreferred(target, width, widget.getSizePolicy().horizontalPolicy);
    }

    public static int getPreferredHeight(int target, Widget widget) {
        final int height = widget.getSizeHint().y + widget.getMargin().vertical();
        return getPreferred(target, height, widget.getSizePolicy().verticalPolicy);
    }

    private static int getPreferred(int target, int hint, SizePolicy.Policy policy) {
        if (target > hint && policy.canGrow) {
            return target;
        } else if (target < hint && policy.canShrink) {
            return target;
        }
        return hint;
    }
}
