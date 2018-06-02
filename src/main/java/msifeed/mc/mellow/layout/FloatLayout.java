package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.widgets.Widget;

import java.util.ArrayList;

public class FloatLayout extends Layout {
    protected ArrayList<LayoutItem> items = new ArrayList<>();

    public FloatLayout(Widget parent) {
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

    @Override
    public void update() {
        for (LayoutItem item : items) {
            final Rect itemGeom = new Rect();
            itemGeom.translate(geometry);
            itemGeom.translate(item.getPos());
            itemGeom.setSize(item.getSizeHint());
            item.setGeometry(itemGeom);
            item.update();
        }
    }
}
