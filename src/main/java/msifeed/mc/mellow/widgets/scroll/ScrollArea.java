package msifeed.mc.mellow.widgets.scroll;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;

public class ScrollArea extends Widget {
    private Part scrollbarBgPart = Mellow.getPart("scrollbar_bg");

    ScrollAreaThumb thumb = new ScrollAreaThumb(this);
    Widget content = new Widget();
    double scroll = 0;

    private Geom scrollbarBgGeom = new Geom();

    public ScrollArea() {
        setLayout(new ScrollAreaLayout());
        setSizePolicy(SizePolicy.Policy.PREFERRED, SizePolicy.Policy.MAXIMUM);

        addChild(content);
        addChild(thumb);
    }

    public void setContent(Widget content) {
        this.content = content;
    }

    public Widget getContent() {
        return content;
    }

    @Override
    protected void updateSelf() {
        scrollbarBgGeom.set(thumb.getGeometry());
        scrollbarBgGeom.y = getGeometry().y;
        scrollbarBgGeom.h = getGeometry().h;
    }

    @Override
    protected void renderSelf() {
        RenderParts.nineSlice(scrollbarBgPart, scrollbarBgGeom);
    }

    @Override
    protected void renderChildren() {
        RenderWidgets.cropped(content, getGeometry());
        thumb.render();
    }

    void updateScroll() {
        scroll = thumb.getPos().y / (double) (getGeometry().h);
    }
}
