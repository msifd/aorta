package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.theme.Part;

import javax.vecmath.Point3f;
import java.util.Collection;

public class Window extends WidgetCollection {
    private Part part = Mellow.THEME.parts.get("window");
    private Header header = new Header(this);
    private WidgetCollection content = new WidgetCollection(this);

    public Window(Widget parent) {
        super(parent);
        setPos(10, 10, 0);
        setSize(200, 100);

        header.setPos(1, 1);
        header.setLabel("Title goes here");
        header.setClickCallback(() -> {
            System.out.println("My header just got clicked!");
        });

        content.setPos(1, 14);
    }

    @Override
    public void setSize(float w, float h) {
        super.setSize(w, h);
        header.setSize(w - 2, Math.min(13, h));
        content.setSize(w - 2, Math.max(0, h - 2 - 13));
    }

    @Override
    public void addChild(Widget widget) {
        content.addChild(widget);
    }

    @Override
    public void removeChild(Widget widget) {
        content.removeChild(widget);
    }

    @Override
    public Collection<Widget> getChildren() {
        return content.getChildren();
    }

    @Override
    protected void renderSelf() {
        RenderParts.nineSlice(part, getAbsPos(), size);
        header.render();
    }

    @Override
    public Widget lookupWidget(Point3f p) {
        if (header.lookupWidget(p) != null)
            return header;
        return super.lookupWidget(p);
    }

    static class Header extends Button {
        Header(Widget parent) {
            super(parent);
        }

        @Override
        protected void renderBackground() {
            if (isHovered())
                RenderShapes.rect(getAbsPos(), size, 0x997577, 80);
        }
    }
}
