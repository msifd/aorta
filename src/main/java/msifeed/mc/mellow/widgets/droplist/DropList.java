package msifeed.mc.mellow.widgets.droplist;

import msifeed.mc.mellow.layout.FreeLayout;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

import java.util.List;
import java.util.function.Consumer;

public class DropList<T> extends Widget {
    final DropListHeader header = new DropListHeader(this);
    final DropListPopup popupList;

    private int selectedItem;
    private final List<T> items;
    private boolean opened = false;

    private Consumer<T> selectCallback = null;

    public DropList(List<T> items) {
        this.items = items;
        this.popupList = new DropListPopup(this);

//        setSizeHint(header.getSizeHint());
        setSizePolicy(header.getSizePolicy());
        setLayout(FreeLayout.INSTANCE);

        selectItem(0);
        header.setZLevel(1);
        popupList.setZLevel(10); // Over other headers
        popupList.setVisible(false);

        addChild(header);
        addChild(popupList);
    }

    @Override
    public Point getContentSize() {
//        return new Point(popupList.getContentSize().x, header.getContentSize().y);

        return header.getContentSize();
    }

    public void selectItem(int i) {
        if (i < items.size()) {
            selectedItem = i;
            header.setLabel(getSelectedItem().toString());
            if (selectCallback != null)
                selectCallback.accept(items.get(i));
        }
    }

    public T getSelectedItem() {
        return items.get(selectedItem);
    }

    public void setSelectCallback(Consumer<T> callback) {
        this.selectCallback = callback;
    }

    @Override
    public boolean containsPoint(Point p) {
        return super.containsPoint(p) || popupList.containsPoint(p);
    }

    boolean isOpened() {
        return opened;
    }

    void setOpened(boolean opened) {
        this.opened = opened;
    }

    List<T> getItems() {
        return items;
    }
}
