package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.MellowGlobal;
import msifeed.mc.mellow.render.RenderShapes;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

public class Widget {
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
        final Widget parent = getParent();
        if (parent != null) {
            final Point3f abs = getParent().getAbsPos();
            abs.add(pos);
            return abs;
        }
        else {
            return (Point3f) pos.clone();
        }
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

    public void setParent(Widget widget) {
        parent = widget;
    }

    public void render() {
        renderSelf();
        if (isHovered())
            renderDebug();
    }

    public void renderDebug() {
        RenderShapes.frame(getAbsPos(), size, 1, hashCode()); // for debug purposes
    }

    protected void renderSelf() {}

    public Widget lookupWidget(Point3f p) {
        final Point3f ap = getAbsPos();
        return p.x >= ap.x && p.x <= ap.x + size.x
                && p.y >= ap.y && p.y <= ap.y + size.y
                && p.z >= ap.z ? this : null;
    }

    public boolean isHovered() {
        return this == MellowGlobal.hoveredWidget;
    }

    public boolean isPressed() {
        return this == MellowGlobal.pressedWidget;
    }
}
