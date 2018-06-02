package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.widgets.Widget;

public class VerticalLayout extends BoxLayout {
    public VerticalLayout(Widget parent) {
        super(parent);
    }

    // TODO: refactor
    @Override
    public void update() {
        int yPos = 0;
        for (LayoutItem item : items) {
            final Point sh = item.getSizeHint();
            final Rect itemGeom = new Rect();

            itemGeom.translate(geometry);
//            itemGeom.translate(item.getPos());
            itemGeom.translate(0, yPos);

            itemGeom.setSize(geometry.w, sh.y);

            item.setGeometry(itemGeom);
            yPos += itemGeom.h;
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
}
