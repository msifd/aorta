package msifeed.mc.aorta.client.gui.auction;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.DragDropHandler;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public class ItemStackSlot extends Widget implements DragDropHandler.Slot<ItemStackWidget> {
    private final int borderColor = Mellow.getColor("text_bright");

    private final boolean locked;
    private ItemStackWidget item = null;
    private Consumer<ItemStackWidget> itemChangeCallback = null;

    ItemStackSlot() {
        this.locked = false;

        setSizeHint(17, 17);
        setSizePolicy(SizePolicy.FIXED);
        getMargin().set(1, 0, 0, 1);
    }

    ItemStackSlot(ItemStack itemStack) {
        this.item = new ItemStackWidget(itemStack);
        this.locked = true;

        setSizeHint(17, 17);
        setSizePolicy(SizePolicy.FIXED);
        getMargin().set(1, 0, 0, 1);

        addChild(item);
    }

    public void setItemChangeCallback(Consumer<ItemStackWidget> callback) {
        this.itemChangeCallback = callback;
    }

    @Override
    protected void renderSelf() {
        RenderShapes.frame(getGeometry(), 1, borderColor);
    }

    @Override
    public boolean canPick() {
        return !locked;
    }

    @Override
    public boolean canDrop(DragDropHandler.Draggable item) {
        return this.item == null && item instanceof ItemStackWidget;
    }

    @Override
    public void drop(DragDropHandler.Draggable item) {
        if (canDrop(item)){
            this.item = (ItemStackWidget) item;
            item.drop(this);
            addChild((ItemStackWidget) item);
            if (itemChangeCallback != null)
                itemChangeCallback.accept((ItemStackWidget) item);
        }
    }

    @Override
    public ItemStackWidget getItem() {
        return item;
    }

    @Override
    public void release() {
        if (item != null) {
            final ItemStackWidget item = this.item;
            this.item = null;
            removeChild(item);
            if (itemChangeCallback != null)
                itemChangeCallback.accept(null);
        }
    }
}
