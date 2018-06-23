package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.DragHandler;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.VerticalLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;

import java.util.Collection;

public class Window extends Widget {
    private Part backgroundPart = Mellow.THEME.parts.get("window");
    private Header header = new Header(this);
    private Widget content = new Widget();

    public Window() {
        setLayout(VerticalLayout.INSTANCE);
        getMargin().set(1);

        header.setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MAXIMUM);
        header.setSizeHint(10, 13);
        header.getMargin().set(3, 0);
        header.setZLevel(1);

        content.getMargin().set(3);
        content.setLayout(VerticalLayout.INSTANCE);

        super.addChild(header);
        super.addChild(content);
    }

    public void setTitle(String text) {
        header.setLabel(text);
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
    protected void renderSelf() {
        RenderParts.nineSlice(backgroundPart, getGeometry());
    }

    static class Header extends Button.AlmostTransparentButton implements MouseHandler.AllBasic {
        protected final DragHandler dragHandler;

        Header(Window window) {
            this.dragHandler = new DragHandler(window);

            getMargin().set(2);
            setLayout(new AnchorLayout(AnchorLayout.Anchor.LEFT, AnchorLayout.Anchor.CENTER));
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
