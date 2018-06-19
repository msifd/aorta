package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class VerticalLayout extends Layout {
    public static final VerticalLayout INSTANCE = new VerticalLayout();

    public int spacing = 1;

    private VerticalLayout() {
    }

    @Override
    public void apply(Widget widget, Collection<Widget> children) {
        final Margins margin = widget.getMargin();
        final Rect geometry = new Rect(widget.getGeometry());
        geometry.offsetPos(margin);
        geometry.offsetSize(margin);

        int yPos = 0;
        for (Widget child : children) {
            final Point sh = child.getLayoutSizeHint();
            final Rect itemGeom = new Rect();

            itemGeom.translate(geometry);
//            itemGeom.translate(item.getPos());
            itemGeom.translate(0, yPos);
            itemGeom.setSize(geometry.w, sh.y);

            child.setGeometry(itemGeom);
            yPos += itemGeom.h;
            yPos += spacing;
        }
    }

//        int fixedHeight = 0;
//        int growableItems = 0;
//        for (LayoutItem item : items) {
//            final SizePolicy.Policy vp = item.getSizePolicy().verticalPolicy;
//            if (!vp.canShrink) {
//                fixedHeight += item.getSizeHint().y;
//            }
//            if (!vp.canGrow) {
//                growableItems++;
//            }
//        }
//
//        int yPos = contents.y;
//        int averageMinHeight = contents.h / countItems();
//        int growableItemHeight = (contents.h - fixedHeight) / growableItems;
//
//        for (LayoutItem item : items) {
//            final SizePolicy.Policy hp = item.getSizePolicy().horizontalPolicy;
//            final SizePolicy.Policy vp = item.getSizePolicy().verticalPolicy;
//            final Point sh = item.getSizeHint();
//            final Rect geom = item.getGeometry();
//
//            geom.x = contents.x;
//            geom.y = yPos;
//            geom.w = Math.min(contents.w, sh.x);
//            geom.h = Math.min(contents.h, sh.y);
//
//            if (hp.canGrow) {
//                geom.w = Math.max(geom.w, contents.w);
//            } else {
//                geom.x = contents.x + (geom.w - sh.x) / 2;
//            }
//
//            if (vp.canShrink) {
//                geom.h = Math.max(geom.h, averageMinHeight);
//            }
//            if (vp.canGrow) {
//                geom.h = Math.max(geom.h, growableItemHeight);
//            }
//
//            item.setGeometry(geom);
//            yPos += geom.h;
//        }
}
