package msifeed.mc.aorta.client.gui.auction;

import msifeed.mc.aorta.economy.bank.Currency;
import msifeed.mc.mellow.handlers.DragDropHandler;
import msifeed.mc.mellow.handlers.DragHandler;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.render.RenderItemStack;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public class ItemStackWidget extends Widget implements DragDropHandler.Draggable, MouseHandler.AllBasic {
    private final DragHandler dragHandler = new DragHandler(this);

    public final ItemStack itemStack;

    ItemStackWidget(ItemStack itemStack) {
        this.itemStack = itemStack;
//        setZLevel(1);
        setSizeHint(16, 16);
        setSizePolicy(SizePolicy.FIXED);
    }

    @Override
    public int getZLevel() {
        return dragHandler.isDragging()
                ? 500
                : super.getZLevel();
    }

    @Override
    protected void renderSelf() {
        final Geom geom = getGeometry();
        final FontRenderer fr = RenderManager.instance.getFontRenderer();

        RenderItemStack.itemStack(itemStack, geom.x, geom.y, geom.z);
        if (itemStack.stackSize > 1)
            RenderWidgets.string(String.valueOf(itemStack.stackSize), geom.x + geom.w - 4, geom.y + geom.h - fr.FONT_HEIGHT, geom.z + 50, 0xffffff, fr);
        if (isHovered())
            RenderItemStack.tooltip(itemStack, Minecraft.getMinecraft().thePlayer, geom.x, geom.y, geom.z);
    }

    @Override
    public void onPress(int xMouse, int yMouse, int button) {
        if (canPick())
            dragHandler.startDrag(new Point(xMouse, yMouse));
        else {
            for (Widget p = getParent(); p != null; p = p.getParent()) {
                if (!(p instanceof MouseHandler.Click)) continue;
                ((MouseHandler.Click) p).onClick(xMouse, yMouse, button);
                break;
            }
        }
    }

    @Override
    public void onMove(int xMouse, int yMouse, int button) {
        dragHandler.drag(new Point(xMouse, yMouse));
    }

    @Override
    public void onRelease(int xMouse, int yMouse, int button) {
        if (!dragHandler.isDragging())
            return;

        final Optional<Widget> droppable = getTopParent()
                .getWidgetsAtPoint(new Point(xMouse, yMouse), DragDropHandler.Droppable.class)
                .findAny();

        if (droppable.isPresent()) {
            final DragDropHandler.Droppable d = (DragDropHandler.Droppable) droppable.get();
            if (d.canDrop(this)) {
                d.drop(this);
                return;
            }
        }

        if (dragHandler.isDragging()) {
            dragHandler.stopDrag();
            setPos(0, 0);
        }
    }

    @Override
    public boolean canPick() {
        if (Currency.isCurrency(itemStack)) return false;

        final Widget p = getParent();
        return !(p instanceof DragDropHandler.Droppable) || ((DragDropHandler.Droppable) p).canPick();
    }

    @Override
    public void drop(DragDropHandler.Droppable droppable) {
        setPos(0, 0);
        final Widget p = getParent();
        if (p != null) {
            if (p instanceof DragDropHandler.Slot)
                ((DragDropHandler.Slot) p).release();
            else
                p.removeChild(this);
        }
        setParent(null);
        dragHandler.stopDrag();
    }
}
