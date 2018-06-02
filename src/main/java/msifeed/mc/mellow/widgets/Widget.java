package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.layout.Layout;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.utils.SizePolicy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class Widget {
    public static Widget focusedWidget = null;
    public static Widget hoveredWidget = null;
    public static Widget pressedWidget = null;

    //    private Point minSize = new Point();
    private Point pos = new Point();
    private Point sizeHint = new Point();
    private SizePolicy sizePolicy = new SizePolicy();
    private Rect geometry = new Rect();

    private int widgetTreeDepth = 0;
    private Widget parent;
    private ArrayList<Widget> children = new ArrayList<>();

    private Layout layout;
    private boolean dirty = true;

    public Widget(Widget parent) {
        setParent(parent);
    }

//    public Point minSize() {
//        return minSize;
//    }
//
//    public void setMinSize(int w, int h) {
//        minSize.set(w, h);
//        setDirty();
//    }


    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos.set(pos);
        setDirty();
    }

    public void setPos(int x, int y) {
        this.pos.set(x, y);
        setDirty();
    }

    public Point getSizeHint() {
        return sizeHint;
    }

    public void setSizeHint(Point sizeHint) {
        this.sizeHint = sizeHint;
    }

    public void setSizeHint(int w, int h) {
        this.sizeHint.set(w, h);
    }

    public SizePolicy getSizePolicy() {
        return sizePolicy;
    }

    public void setSizePolicy(SizePolicy sizePolicy) {
        this.sizePolicy = sizePolicy;
        setDirty();
    }

    public void setSizePolicy(SizePolicy.Policy h, SizePolicy.Policy v) {
        this.sizePolicy.horizontalPolicy = h;
        this.sizePolicy.verticalPolicy = v;
        setDirty();
    }

    public Rect getGeometry() {
        return geometry;
    }

    public void setGeometry(Rect g) {
        this.geometry = g;
        if (this.layout != null)
            this.layout.setGeometry(g);
        setDirty();
    }

    public Widget getParent() {
        return parent;
    }

    public void setParent(Widget parent) {
        if (parent != null) {
            this.parent = parent;
            this.widgetTreeDepth = parent.widgetTreeDepth + 1;
        } else {
            this.parent = null;
        }
        setDirty();
    }

    public int getWidgetTreeDepth() {
        return widgetTreeDepth;
    }

    public void addChild(Widget widget) {
        children.add(widget);
        setDirty();
    }

    public void removeChild(Widget widget) {
        children.remove(widget);
        setDirty();
    }

    public Collection<Widget> getChildren() {
        return children;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        setDirty();
    }

    public void setDirty() {
        this.dirty = true;
        if (parent != null)
            this.parent.dirty = true;
        for (Widget w : getChildren())
            w.setDirty();
    }

    public void update() {
        if (dirty) {
            updateSelf();
            if (layout != null)
                layout.update();
            dirty = false;
            for (Widget c : getChildren())
                c.update();
        }
    }

    public void render() {
        renderSelf();
        renderChildren();
        if (isHovered())
            renderDebug();
    }

    public void renderDebug() {
        RenderShapes.frame(getGeometry(), 1, hashCode()); // for debug purposes
    }

    protected void updateSelf() {
    }

    protected void renderSelf() {
    }

    protected void renderChildren() {
        for (Widget w : getChildren())
            w.render();
    }

    public Optional<Widget> childAt(Point p) {
        for (Widget w : getChildren()) {
            if (w.geometry.contains(p))
                return Optional.of(w);
        }
        return Optional.empty();
    }

    public boolean isHovered() {
        return this == hoveredWidget;
    }

    public boolean isPressed() {
        return this == pressedWidget;
    }
}
