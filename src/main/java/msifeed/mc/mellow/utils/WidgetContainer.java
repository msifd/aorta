package msifeed.mc.mellow.utils;

import msifeed.mc.mellow.widgets.Widget;

import java.util.ArrayList;
import java.util.Collection;

public class WidgetContainer {
    protected ArrayList<Widget> children = new ArrayList<>();

    public void addChild(Widget widget) {
        children.add(widget);
    }

    public void removeChild(Widget widget) {
        children.remove(widget);
    }

    public Collection<Widget> getChildren() {
        return children;
    }
}
