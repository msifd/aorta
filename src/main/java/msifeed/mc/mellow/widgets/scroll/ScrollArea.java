package msifeed.mc.mellow.widgets.scroll;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.layout.FreeLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.widgets.Widget;

public class ScrollArea extends Widget {
    private Part scrollbarBgPart = Mellow.THEME.parts.get("scrollbar_bg");

    private ScrollAreaThumb thumb = new ScrollAreaThumb();
    private Widget content = new Widget();

    private Geom scrollbarBgGeom = new Geom();
    private Geom visibleContentGeom = new Geom();

    private double scroll = 0;

    public ScrollArea() {
//        getMargin().right = scrollbarBgPart.size.x;
        setLayout(FreeLayout.INSTANCE);

        content.getMargin().set(1);

//        scrollbarBgGeom.setSize(scrollbarBgPart.size);

        addChild(content);
        addChild(thumb);
    }

    public Widget getContent() {
        return content;
    }

//    @Override
//    public void addChild(Widget widget) {
//        content.addChild(widget);
//    }


//    @Override
//    protected void updateIndepenent() {
//        super.updateIndepenent();
//
//        final Geom geom = getGeometry();
//
////        content.getSizeHint().x = geom.w - scrollbarBgPart.size.x;
//
////        scrollbarBgGeom.set(geom);
//        scrollbarBgGeom.x = geom.x + geom.w - scrollbarBgPart.size.x;
//        scrollbarBgGeom.y = geom.y;
//        scrollbarBgGeom.h = geom.h;
//
//        visibleContentGeom.set(geom);
//        visibleContentGeom.w -= scrollbarBgGeom.w;
//
//        thumb.setPos(visibleContentGeom.w + 1, 1);
////        final double contentRelHeight = geom.h / (double) content.getGeometry().h;
////        final int thumbAreaHeight = geom.h - 2;
////        thumbGeom.x = 1 + scrollbarBgGeom.x;
////        thumbGeom.h = (int) Math.floor(geom.h * contentRelHeight);
////        thumbGeom.y = 1 + geom.y + (int) Math.floor(scroll * (thumbAreaHeight - thumbGeom.h));
//
////        content.getSizeHint().x = visibleContentGeom.w;
////        content.setDirty();
//
////        contentGeom.setSize(content.getLayoutSizeHint());
////        contentGeom.w = geom.w - scrollbarBgGeom.w;
////        contentGeom.h = geom.h;
//    }

    @Override
    protected void renderSelf() {
        RenderParts.nineSlice(scrollbarBgPart, scrollbarBgGeom);
    }

    @Override
    protected void renderChildren() {
        RenderWidgets.cropped(content, visibleContentGeom);
        thumb.render();
    }

}
