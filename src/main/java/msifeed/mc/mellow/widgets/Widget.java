package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.layout.FloatLayout;
import msifeed.mc.mellow.layout.Layout;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.utils.*;

import java.util.Collection;
import java.util.stream.Collectors;

public class Widget extends WidgetContainer {
    public static Widget hoveredWidget = null;
    public static Widget pressedWidget = null;
    protected static Widget focusedWidget = null;

    protected Layout layout = FloatLayout.INSTANCE;

    protected boolean visible = true;
    private Point pos = new Point();
    private int zLevel = 0;
    private Point sizeHint = new Point();
    private SizePolicy sizePolicy = new SizePolicy();
    private Margins margin = new Margins();

    private boolean dirty = true;
    private Geom geometry = new Geom();

    private Widget parent;
    private int widgetTreeDepth = 0;

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

    public Point getLayoutSizeHint() {
        return layout.getSizeOfContent(this);
    }

    public void setSizeHint(int w, int h) {
        this.sizeHint.set(w, h);
        setDirty();
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

    public Widget getParent() {
        return parent;
    }

    public void setParent(Widget parent) {
        if (parent != null) {
            this.parent = parent;
        } else {
            this.parent = null;
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

    public void setDirty() {
        this.dirty = true;
    }

    public boolean isVisible() {
        return visible;
    }

    public void update() {
        if (dirty) {
            dirty = false;
            if (parent != null)
                widgetTreeDepth = parent.widgetTreeDepth + 1;
            updateLayout();
            updateSelf();
        }
        for (Widget c : children)
            c.update();
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

    protected void updateSelf() {
    }

    protected void updateLayout() {
        layout.apply(this, children);
    }

    protected void renderSelf() {
    }

    protected void renderChildren() {
        for (Widget w : children)
            w.render();
    }

    @Override
    public void addChild(Widget widget) {
        super.addChild(widget);
        setDirty();
        if (widget.parent == null) {
            widget.setParent(this);
        }
    }

    @Override
    public void removeChild(Widget widget) {
        super.removeChild(widget);
        setDirty();
    }

    protected Collection<Widget> getLookupChildren() {
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

    public boolean isHigherThan(Widget another) {
        return geometry.z > another.geometry.z
                || (geometry.z == another.geometry.z && widgetTreeDepth > another.widgetTreeDepth);
    }
}
