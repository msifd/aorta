package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.layout.Layout;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class Widget extends WidgetContainer {
    public static Widget focusedWidget = null;
    public static Widget hoveredWidget = null;
    public static Widget pressedWidget = null;

    private Point pos = new Point();
    private Point sizeHint = new Point();
    private SizePolicy sizePolicy = new SizePolicy();
    private Margins margin = new Margins();
    private Rect geometry = new Rect();

    private int widgetTreeDepth = 0;
    private Widget parent;

    protected Layout layout = Layout.NONE;
    protected boolean visible = true;
    private boolean dirty = true;
    
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

    public Point getLayoutSizeHint() {
        return layout.sizeHintOfContent(this);
    }

    public void setSizeHint(Point sizeHint) {
        this.sizeHint = sizeHint;
        setDirty();
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

    public Margins getMargin() {
        return margin;
    }

    public void setMargin(Margins margin) {
        this.margin = margin;
        setDirty();
    }

    public Rect getGeometry() {
        return geometry;
    }

    public void setGeometry(Rect g) {
        this.geometry = g;
        setDirty();
    }

    public Widget getParent() {
        return parent;
    }

    public void setParent(Widget parent) {
        if (parent != null) {
            this.parent = parent;
            this.widgetTreeDepth = this.parent.widgetTreeDepth + 1;
        } else {
            this.parent = null;
        }
        setDirty();
    }

    public int getWidgetTreeDepth() {
        return widgetTreeDepth;
    }

//    public Layout getLayout() {
//        return layout;
//    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        setDirty();
    }

    public void setDirty() {
        this.dirty = true;
    }

    public void update() {
        if (dirty) {
            dirty = false;
            updateLayout();
            updateSelf();
            for (Widget c : children)
                c.update();
        }
    }

    public void render() {
        if (visible) {
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

    public Optional<Widget> childAt(Point p) {
        for (Widget w : children) {
            if (w.visible && w.geometry.contains(p))
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

    public boolean isFocused() {
        return this == focusedWidget;
    }
}
