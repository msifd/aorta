package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.layout.FloatLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Point;

public class ScrollArea extends Widget {
    private Part bgPart = Mellow.THEME.parts.get("scrollbar_bg");

    private Thumb thumb = new Thumb();
    private Widget content = new Widget();

    private Geom bgGeom = new Geom();
    private Geom visibleContentGeom = new Geom();
    private Geom thumbGeom = thumb.getGeometry();
    private Geom contentGeom = content.getGeometry();

    private double scroll = 0;

    public ScrollArea() {
//        getMargin().right = bgPart.size.x;
        setLayout(FloatLayout.INSTANCE);

        content.getMargin().set(1);

        addChild(content);
        addChild(thumb);
    }

    @Override
    public Point getLayoutSizeHint() {
        return content.getLayoutSizeHint();
    }

    public Widget getContent() {
        return content;
    }

//    @Override
//    public void addChild(Widget widget) {
//        content.addChild(widget);
//    }


    @Override
    protected void updateLayout() {
        super.updateLayout();

        final Geom geom = getGeometry();

        bgGeom.set(geom);
        bgGeom.x = geom.x + geom.w - bgPart.size.x;
        bgGeom.w = bgPart.size.x;

        visibleContentGeom.set(geom);
        visibleContentGeom.w -= bgGeom.w;

        final double contentRelHeight = geom.h / (double) content.getGeometry().h;
        final int thumbAreaHeight = geom.h - 2;
        thumbGeom.x = 1 + bgGeom.x;
        thumbGeom.h = (int) Math.floor(geom.h * contentRelHeight);
        thumbGeom.y = 1 + geom.y + (int) Math.floor(scroll * (thumbAreaHeight - thumbGeom.h));

//        content.getSizeHint().x = visibleContentGeom.w;
//        content.setDirty();

//        contentGeom.setSize(content.getLayoutSizeHint());
        contentGeom.w = geom.w - bgGeom.w;
//        contentGeom.h = geom.h;
    }

    @Override
    protected void renderSelf() {
        RenderParts.nineSlice(bgPart, bgGeom);
    }

    @Override
    protected void renderChildren() {
        RenderWidgets.cropped(content, visibleContentGeom);
        thumb.render();
    }

    private static class Thumb extends Button {
        private Part thumbPart = Mellow.THEME.parts.get("scrollbar_thumb");
        private Part thumbHoverPart = Mellow.THEME.parts.get("scrollbar_thumb_hover");

        Thumb() {
            setSizeHint(thumbPart.size);
        }

        @Override
        protected void renderSelf() {
            if (isHovered())
                RenderParts.nineSlice(thumbHoverPart, getGeometry());
            else
                RenderParts.nineSlice(thumbPart, getGeometry());
        }
    }
}
