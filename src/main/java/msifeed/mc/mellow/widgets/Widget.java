package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.layout.Layout;
import msifeed.mc.mellow.utils.Offset;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.Rect;

import java.util.ArrayList;
import java.util.Collection;

public class Widget {
    public static Widget focusedWidget = null;
    public static Widget hoveredWidget = null;
    public static Widget pressedWidget = null;

    protected Point minSize = new Point();
    protected Offset margin = new Offset();
    protected Offset padding = new Offset();
    private Rect bounds = new Rect();

    private int depth = 0;
    private Widget parent;
    private ArrayList<Widget> children = new ArrayList<>();

    private Layout layout;
    private boolean dirty = true;

    public Widget(Widget parent) {
        setParent(parent);
    }

    public Point getMinSize() {
        return minSize;
    }

    public Offset getMargin() {
        return margin;
    }

    public Offset getPadding() {
        return padding;
    }

    public void setMargin(int x, int y) {
        margin.setTopLeft(x, y);
        markDirty();
    }

    public void setMinSize(int w, int h) {
        minSize.set(w, h);
        markDirty();
    }

    public Rect getBounds() {
        return bounds;
    }

    public void setBounds(Rect bounds) {
        this.bounds = bounds;
    }

    public void setBounds(int x, int y, int w, int h) {
        this.bounds.setPos(x, y, w, h);
    }

    public Widget getParent() {
        return parent;
    }

    public void setParent(Widget parent) {
        if (parent != null) {
            final Widget container = parent.getContainer();
            this.parent = container;
            this.depth = container.depth + 1;
        } else {
            this.parent = null;
        }
        markDirty();
    }

    public int getDepth() {
        return depth;
    }

    public Widget getContainer() {
        return this;
    }

    public void addChild(Widget widget) {
        children.add(widget);
        markDirty();
    }

    public void removeChild(Widget widget) {
        children.remove(widget);
        markDirty();
    }

    public Collection<Widget> getChildren() {
        return children;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        this.layout.bind(this);
        markDirty();
    }

    public void markDirty() {
        this.dirty = true;
        for (Widget w : getChildren())
            w.markDirty();
//        if (parent != null)
//            parent.dirty = true;
    }

    public void update() {
        if (dirty) {
            if (parent != null && parent.layout != null)
                parent.layout.update();
            dirty = false;
            updateSelf();
        }
        for (Widget w : getChildren())
            w.update();
    }

    public void render() {
        renderSelf();
        renderChildren();
        if (isHovered())
            renderDebug();
    }

    public void renderDebug() {
//        RenderShapes.frame(getAbsPos(), size, 1, hashCode()); // for debug purposes
    }

    protected void updateSelf() {
    }

    protected void renderSelf() {
    }

    protected void renderChildren() {
        for (Widget w : getChildren())
            w.render();
    }

    public void addChildrenAt(Collection<Widget> widgets, Point p) {
        for (Widget w : getChildren()) {
            if (w.bounds.contains(p))
                widgets.add(w);
            w.addChildrenAt(widgets, p);
        }
    }

    public boolean isHovered() {
        return this == hoveredWidget;
    }

    public boolean isPressed() {
        return this == pressedWidget;
    }
}
