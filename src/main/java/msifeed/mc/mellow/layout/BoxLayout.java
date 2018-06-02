package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.widgets.Widget;

import java.util.ArrayList;

public abstract class BoxLayout extends Layout {
    protected ArrayList<LayoutItem> items = new ArrayList<>();

    public BoxLayout(Widget parent) {
        super(parent);
    }

    @Override
    public void addItem(LayoutItem item) {
        items.add(item);
    }

    @Override
    public void removeItem(LayoutItem item) {
        items.remove(item);
    }

    @Override
    public int countItems() {
        return items.size();
    }
}
