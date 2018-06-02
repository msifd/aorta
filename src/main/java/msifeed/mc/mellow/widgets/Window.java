package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.DragHandler;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.FloatLayout;
import msifeed.mc.mellow.layout.Layout;
import msifeed.mc.mellow.layout.VerticalLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;

public class Window extends Widget {
    private Part part = Mellow.THEME.parts.get("window");
    private Header header = new Header(this);
    private Layout contentLayout = new FloatLayout(this);

    public Window(Widget parent) {
        super(parent);

        final VerticalLayout layout = new VerticalLayout(this);
        layout.getMargin().set(1);
        super.setLayout(layout); // Window' setLayout returns content layout

        header.setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MAXIMUM);
        header.setSizeHint(10, 13);

        layout.addWidget(header);
        layout.addLayout(contentLayout);

//        final VerticalLayout content = new VerticalLayout(this);
//        content.getMargin().set(2);
    }

    public void setTitle(String text) {
        header.setLabel(text);
    }

    @Override
    public Layout getLayout() {
        return contentLayout;
    }

    @Override
    public void setLayout(Layout layout) {
        final Layout baseLayout = super.getLayout();
        baseLayout.removeItem(contentLayout);
        baseLayout.addLayout(layout);
        contentLayout = layout;
        setDirty();
    }

    @Override
    protected void renderSelf() {
        RenderParts.nineSlice(part, getGeometry());
    }

    static class Header extends Button implements MouseHandler.AllBasic {
        protected final DragHandler dragHandler;

        Header(Widget parent) {
            super(parent);
            dragHandler = new DragHandler(parent);

            final AnchorLayout layout = new AnchorLayout(this, AnchorLayout.Anchor.LEFT, AnchorLayout.Anchor.TOP);
            layout.getMargin().set(2);
            layout.addWidget(label);
            setLayout(layout);
        }

        @Override
        protected void renderBackground() {
            if (isHovered())
                RenderShapes.rect(getGeometry(), 0x997577, 80);
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
