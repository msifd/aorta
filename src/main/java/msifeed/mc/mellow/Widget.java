package msifeed.mc.mellow;

import com.google.common.eventbus.EventBus;
import msifeed.mc.mellow.render.RenderShapes;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

public abstract class Widget {
    public Point3f pos = new Point3f();
    public Point2f size = new Point2f();

    private Widget parent;

    public Widget(Widget parent) {
        setParent(parent);
    }

    public void setPos(float x, float y) {
        pos.set(x, y, pos.z);
    }

    public void setPos(float x, float y, float z) {
        pos.set(x, y, z);
    }

    public Point3f getPos() {
        return pos;
    }

    public Point3f getAbsPos() {
        final Point3f rel = (Point3f) getParent().getAbsPos().clone();
        rel.add(pos);
        return rel;
    }

    public void setSize(float w, float h) {
        size.set(w, h);
    }

    public Point2f getSize() {
        return size;
    }

    public Widget getParent() {
        return parent;
    }

    public Widget getTopWidget() {
        return parent.getTopWidget();
    }

    public void setParent(Widget widget) {
        if (parent != null)
            parent.getEventBus().unregister(this);
        parent = widget;
        if (parent != null)
            parent.getEventBus().register(this);
    }

    public void render() {
        renderSelf();
//        renderDebug();
    }

    public void renderDebug() {
        RenderShapes.frame(getAbsPos(), size, 1, hashCode()); // for debug purposes
    }

    protected abstract void renderSelf();

    protected EventBus getEventBus() {
        return parent.getEventBus();
    }

    public Widget lookupWidget(Point3f p) {
        final Point3f ap = getAbsPos();
        return p.x >= ap.x && p.x <= ap.x + size.x
                && p.y >= ap.y && p.y <= ap.y + size.y
                && p.z >= ap.z ? this : null;
    }
}
