package msifeed.mc.mellow.widgets.window;

import msifeed.mc.mellow.handlers.DragHandler;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.button.Button;
import msifeed.mc.mellow.widgets.button.ButtonLabel;

class WindowHeader extends ButtonLabel implements MouseHandler.AllBasic {
    private final DragHandler dragHandler;

    WindowHeader(Window window) {
        this.dragHandler = new DragHandler(window);

        getMargin().set(3);
        setLayout(new AnchorLayout(AnchorLayout.Anchor.LEFT, AnchorLayout.Anchor.CENTER));
    }

    @Override
    protected void renderSelf() {
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
