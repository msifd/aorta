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

    protected Widget parent = null;
    protected ArrayList<Widget> children = null;
    protected EventBus childrenEventBus = null;

    protected Part part = null;

    public Widget() {
        part = Mellow.THEME.parts.get(getPartName());
//        Mellow.EVENT_BUS.register(this);
    }

    public void addChild(Widget widget) {
        if (children == null) {
            children = new ArrayList<>();
            childrenEventBus = new EventBus();
        }
        children.add(widget);
        childrenEventBus.register(widget);
    }

    public void removeChild(Widget widget) {
        if (children == null)
            return;
        children.remove(widget);
        childrenEventBus.unregister(widget);
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

    public String getPartName() {
        return null;
    }

    public Part getPart() {
        return part;
    }

    public void render() {
        renderSelf();
        renderChildren();
    }

    public abstract void renderSelf();

    public void renderChildren() {
        if (children != null) {
            for (Widget w : children)
                w.render();
        }
    }
}
