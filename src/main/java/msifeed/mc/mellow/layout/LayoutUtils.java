package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;

public class LayoutUtils {
    public static Geom getGeomWithMargin(Widget widget) {
        final Geom geometry = new Geom(widget.getGeometry());
        final Margins margin = widget.getMargin();
        geometry.offsetPos(margin);
        geometry.offsetSize(margin);
        return geometry;
    }

    public static Point getPreferredContentSize(Widget widget) {
        final Point target = widget.getContentSize();
        final Point p = new Point();
        p.x = getPreferredWidth(target.x, widget);
        p.y = getPreferredHeight(target.y, widget);
        return p;
    }

    public static Point getPreferredSize(Point target, Widget widget) {
        final Point p = new Point();
        p.x = getPreferredWidth(target.x, widget);
        p.y = getPreferredHeight(target.y, widget);
        return p;
    }

    public static int getPreferredWidth(int target, Widget widget) {
        return getPreferred(target, widget.getSizeHint().x, widget.getSizePolicy().horizontalPolicy);
    }

    public static int getPreferredHeight(int target, Widget widget) {
        return getPreferred(target, widget.getSizeHint().y, widget.getSizePolicy().verticalPolicy);
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
