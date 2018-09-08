package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.layout.FreeLayout;
import msifeed.mc.mellow.layout.Layout;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Widget {
    public static Widget hoveredWidget = null;
    public static Widget pressedWidget = null;
    protected static Widget focusedWidget = null;

    private boolean visible = true;
    private boolean dirty = true;

    private int zLevel = 0;
    private Point pos = new Point();
    private Point sizeHint = new Point();
    private Margins margin = new Margins();
    private SizePolicy sizePolicy = new SizePolicy();

    private Layout layout = FreeLayout.INSTANCE;
    private Point contentSize = new Point();
    private Geom geometry = new Geom();

    private Widget parent;
    private int widgetTreeDepth = 0;
    private ArrayList<Widget> children = new ArrayList<>();

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isDirtyTree() {
        Widget wit = this;
        do {
            if (wit.dirty)
                return true;
            wit = wit.parent;
        } while (wit != null);
        return false;
    }

    public void setDirty() {
        dirty = true;
//        Widget wit = this;
//        do {
//            wit.dirty = true;
//            wit = wit.parent;
//        } while (wit != null && !wit.dirty);
    }

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

    public int getZLevel() {
        return zLevel;
    }

    public void setZLevel(int z) {
        this.zLevel = z;
    }

    public Point getSizeHint() {
        return sizeHint;
    }

    public void setSizeHint(Point sizeHint) {
        this.sizeHint.set(sizeHint);
        setDirty();
    }

    public void setSizeHint(int w, int h) {
        this.sizeHint.set(w, h);
        setDirty();
    }

    public Point getContentSize() {
        return contentSize;
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

    public void setVerSizePolicy(SizePolicy.Policy v) {
        this.sizePolicy.verticalPolicy = v;
        setDirty();
    }

    public Margins getMargin() {
        return margin;
    }

    public void setMargin(Margins margin) {
        this.margin = margin;
        setDirty();
    }

    public Geom getGeometry() {
        return geometry;
    }

    public int getGeometryZ() {
        return getGeometry().z;
    }

    public Widget getParent() {
        return parent;
    }

    public void setParent(Widget parent) {
        if (parent != null) {
            this.parent = parent;
//            this.widgetTreeDepth = parent.widgetTreeDepth + 1;
        } else {
            this.parent = null;
//            this.widgetTreeDepth = 0;
        }
        setDirty();
    }

    public int getWidgetTreeDepth() {
        return widgetTreeDepth;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        setDirty();
    }

    public void update() {
//        if (dirty) {
//            dirty = false;
//            if (parent != null)
//                widgetTreeDepth = parent.widgetTreeDepth + 1;
//            layout.layoutRelativeParent(this, children);
//            contentSize = layout.layoutIndependent(children);
//            updateSelf();
////            updateIndepenent();
//        }
//
//        for (Widget c : children)
//            c.update();

//        setDirty();
        dirty = true;

        updateIndependentLayout();
        updateRelativeLayout();

        dirty = false;
    }

    protected void updateIndependentLayout() {
//        if (!isDirtyTree() || children.isEmpty())
        if (children.isEmpty())
            return;

        for (Widget child : children)
            child.updateIndependentLayout();

        contentSize = layout.layoutIndependent(this, children);
    }

    protected void updateRelativeLayout() {
//        if (!isDirtyTree() || children.isEmpty())
        if (children.isEmpty())
            return;

        updateWidgetTreeDepth();

        layout.layoutRelativeParent(this, children);

        for (Widget child : children)
            child.updateRelativeLayout();

        updateSelf();

//        System.out.println(toString());

        dirty = false;
    }

    protected void updateWidgetTreeDepth() {
        if (parent != null)
            this.widgetTreeDepth = parent.widgetTreeDepth + 1;
        else
            this.widgetTreeDepth = 0;
    }

    protected void updateSelf() {
    }

    public void render() {
        if (isVisible()) {
            renderSelf();
            renderChildren();
            if (isHovered())
                renderDebug();
        }
    }

    public void renderDebug() {
        RenderShapes.frame(getGeometry(), 1, hashCode()); // for debug purposes
    }

    protected void renderSelf() {
    }

    protected void renderChildren() {
        for (Widget w : children)
            w.render();
    }

    public Collection<Widget> getChildren() {
        return children;
    }

    public void addChild(Widget widget) {
        children.add(widget);
        setDirty();
        if (widget.parent == null) {
            widget.setParent(this);
        }
    }

    public void removeChild(Widget widget) {
        children.remove(widget);
        setDirty();
    }

    public void clearChildren() {
        children.clear();
    }

    public Collection<Widget> getLookupChildren() {
        return getChildren();
    }

    public boolean containsPoint(Point p) {
        return isVisible() && getGeometry().contains(p);
    }

    public Collection<Widget> childrenAt(Point p) {
        return children.stream()
                .filter(w -> w.containsPoint(p))
                .collect(Collectors.toList());
    }

    public boolean isHovered() {
        return this == hoveredWidget;
    }

    public boolean isPressed() {
        return this == pressedWidget;
    }

    public boolean isFocused() {
        return this == focusedWidget;
    }

    public static void setFocused(Widget widget) {
        if (focusedWidget == widget)
            return;
        final Widget lastFocus = focusedWidget;
        focusedWidget = widget;
        if (lastFocus != null)
            lastFocus.onFocusLoss();
    }

    protected void onFocusLoss() {
    }

    public int isHigherThan(Widget another) {
        return Comparator.comparingInt(Widget::getGeometryZ)
            .thenComparing(Widget::getWidgetTreeDepth)
            .compare(this, another);
    }
}
