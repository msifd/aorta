package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class VerticalLayout extends Layout {
    public static final VerticalLayout INSTANCE = new VerticalLayout(1);

    public final int spacing;

    public VerticalLayout(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void apply(Widget widget, Collection<Widget> children) {
        final Margins margin = widget.getMargin();
        final Geom geometry = new Geom(widget.getGeometry());
        geometry.offsetPos(margin);
        geometry.offsetSize(margin);

        int yPos = 0;
        for (Widget child : children) {
            final Point sh = child.getLayoutSizeHint();
            final SizePolicy sp = child.getSizePolicy();
            final Geom childGeom = child.getGeometry();

//            final int width = sp.horizontalPolicy.canShrink ? geometry.w : Math.max(geometry.w, sh.x);
            final int width;
            {
                final int target = geometry.w;
                final int hint = sh.x;
                final SizePolicy.Policy policy = sp.horizontalPolicy;
                if (target > hint && policy.canGrow) {
                    width = target;
                } else if (target < hint && policy.canShrink) {
                    width = target;
                } else {
                    width = hint;
                }
            }

            childGeom.set(geometry);
            childGeom.translate(0, yPos, child.getZLevel());
            childGeom.translate(child.getPos());
            childGeom.setSize(width, sh.y);
            child.setDirty();

            yPos += childGeom.h;
            yPos += spacing;
        }
    }
}
