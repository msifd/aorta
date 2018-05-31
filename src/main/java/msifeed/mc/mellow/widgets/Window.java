package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.DragHandler;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.FloatLayout;
import msifeed.mc.mellow.layout.Layout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.Rect;

public class Window extends Widget {
    private Part part = Mellow.THEME.parts.get("window");
    private Header header = new Header(this);
    private Widget content = new Widget(this);

    public Window(Widget parent) {
        super(parent);
        padding.set(1);
        super.setLayout(new FloatLayout());

        setMargin(10, 10);
        setMinSize(200, 100);

        header.setLabel("Title goes here");

        content.setMargin(0, 14);
        content.setLayout(new FloatLayout());

        Button btn = new Button(content, "pew");
        btn.setMinSize(100, 50);
        btn.setClickCallback(() -> System.out.println("meow!"));
        content.addChild(btn);

        addChild(header);
        addChild(content);
    }

    @Override
    public void setLayout(Layout layout) {
        content.setLayout(layout);
    }

    @Override
    protected void updateSelf() {
        final Rect b = getBounds();
        header.setMinSize(b.w - 2, Math.min(13, b.h));
        content.setMinSize(b.w - 2, Math.max(0, b.h - 2 - 13));
    }

    @Override
    protected void renderSelf() {
        RenderParts.nineSlice(part, getBounds());
    }

    static class Header extends Button implements MouseHandler.AllBasic {
        protected final DragHandler dragHandler;

        Header(Widget parent) {
            super(parent);
            dragHandler = new DragHandler(parent);
            setLayout(new AnchorLayout(AnchorLayout.Anchor.LEFT, AnchorLayout.Anchor.TOP));
        }

        @Override
        protected void renderBackground() {
            if (isHovered())
                RenderShapes.rect(getBounds(), 0x997577, 80);
        }

        @Override
        public void onPress(int xMouse, int yMouse, int button) {
            dragHandler.startDrag(new Point(xMouse, yMouse));
        }

        @Override
        public void onMove(int xMouse, int yMouse, int button) {
            dragHandler.drag(new Point(xMouse, yMouse));
        }

        @Override
        public void onRelease(int xMouse, int yMouse, int button) {
            dragHandler.stopDrag();
        }
    }
}
