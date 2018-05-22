package msifeed.mc.mellow;

import java.util.ArrayList;
import java.util.Collection;

public abstract class WidgetCollection extends Widget {
    protected ArrayList<Widget> children = new ArrayList<>();

    public WidgetCollection(Widget parent) {
        super(parent);
    }

    public void addChild(Widget widget) {
        children.add(widget);
        widget.setParent(this);
    }

    public void removeChild(Widget widget) {
        children.remove(widget);
    }

    public Collection<Widget> getChildren() {
        return children;
    }

    @Override
    protected void renderSelf() {
        renderChildren();
    }

    protected void renderChildren() {
        if (children != null) {
            for (Widget w : getChildren())
                w.render();
        }
    }
}
