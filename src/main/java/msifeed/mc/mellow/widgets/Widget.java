package msifeed.mc.mellow.widgets;

import com.google.common.eventbus.EventBus;
import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.theme.Part;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import java.util.ArrayList;
import java.util.Collection;

public abstract class Widget {
    public Point3f pos = new Point3f();
    public Point2f size = new Point2f();

    private Widget parent = null;
    protected ArrayList<Widget> children = null;

    public Widget(Widget parent) {
        this.parent = parent;
    }

    public void addChild(Widget widget) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(widget);
        widget.setParent(this);
    }

    public void removeChild(Widget widget) {
        if (children == null)
            return;
        children.remove(widget);
    }

    public Collection<Widget> getChildren() {
        return children;
    }

    public void setParent(Widget widget) {
        parent = widget;
    }

    public Widget getParent() {
        return parent;
    }

    public void render() {
        renderSelf();
        renderChildren();
    }

    public abstract void renderSelf();

    public void renderChildren() {
        if (children != null) {
            for (Widget w : getChildren())
                w.render();
        }
    }
}
