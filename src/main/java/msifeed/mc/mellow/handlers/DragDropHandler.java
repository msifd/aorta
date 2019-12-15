package msifeed.mc.mellow.handlers;

public class DragDropHandler {
    public interface Droppable {
        boolean canPick();
        boolean canDrop(Draggable item);
        void drop(Draggable item);
    }

    public interface Draggable {
        boolean canPick();
        void drop(Droppable droppable);
    }

    public interface Slot<T extends Draggable> extends Droppable {
        T getItem();
        void release();
    }
}
