package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.layout.FloatLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Rect;

public class Window extends Widget {
    private Part part = Mellow.THEME.parts.get("window");
    private Header header = new Header(this);
    private Widget content = new Widget(this);

    public Window(Widget parent) {
        super(parent);
        padding.set(1);
        setLayout(new FloatLayout());

        setMargin(10, 10);
        setMinSize(200, 100);

//        header.setMargin(1, 1);
        header.setLabel("Title goes here");
        header.setClickCallback(() -> {
            System.out.println("My header just got clicked!");
        });

        content.setMargin(1, 14);

        addChild(header);
        addChild(content);
    }

//    @Override
//    public void setSize(float w, float h) {
//        super.setSize(w, h);
//        header.setSize(w - 2, Math.min(13, h));
//        content.setSize(w - 2, Math.max(0, h - 2 - 13));
//    }

    @Override
    protected void updateSelf() {
        final Rect b = getBounds();
        header.setMinSize(b.w - 2, Math.min(13, b.h));
    }

    @Override
    protected void renderSelf() {
        RenderParts.nineSlice(part, getBounds());
    }

    static class Header extends Button {
        Header(Widget parent) {
            super(parent);
        }

        @Override
        protected void renderBackground() {
            if (isHovered())
                RenderShapes.rect(getBounds(), 0x997577, 80);
        }
    }
}
