package msifeed.mc.mellow.widgets.scroll;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.DragHandler;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.button.Button;
import net.minecraft.util.MathHelper;

class ScrollAreaThumb extends Button implements MouseHandler.AllBasic {
    private Part thumbPart = Mellow.getPart("scrollbar_thumb");
    private Part thumbHoverPart = Mellow.getPart("scrollbar_thumb_hover");

    private final ScrollArea scrollArea;
    private final DragHandler dragHandler = new DragHandler(this);

    ScrollAreaThumb(ScrollArea scrollArea) {
        this.scrollArea = scrollArea;
        setZLevel(1);
        setSizeHint(thumbPart.size);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
    }

    @Override
    protected void renderSelf() {
        if (isHovered() || isPressed())
            RenderParts.nineSlice(thumbHoverPart, getGeometry());
        else
            RenderParts.nineSlice(thumbPart, getGeometry());
    }

    @Override
    public void onPress(int xMouse, int yMouse, int button) {
        dragHandler.startDrag(new Point(0, yMouse));
    }

    @Override
    public void onMove(int xMouse, int yMouse, int button) {
        dragHandler.drag(new Point(0, yMouse));
        clampYPos();
    }

    @Override
    public void onRelease(int xMouse, int yMouse, int button) {
        dragHandler.stopDrag();
        clampYPos();
    }

    void clampYPos() {
        final Point pos = getPos();
        final int maxY = scrollArea.getGeometry().h - getGeometry().h;
        pos.y = MathHelper.clamp_int(pos.y, 0, maxY);
    }
}
