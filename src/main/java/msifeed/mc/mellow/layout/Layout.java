package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Optional;

public abstract class Layout extends LayoutItem {
    protected final Widget parent;
    protected Margins margin = new Margins();
    protected Rect geometry = new Rect();

    public Layout(Widget parent) {
        this.parent = parent;
    }

    public Margins getMargin() {
        return margin;
    }

    public void setMargin(Margins margin) {
        this.margin = margin;
    }

    @Override
    public Point getPos() {
        return parent.getPos();
    }

    @Override
    public Point getSizeHint() {
        return parent.getSizeHint();
    }

    @Override
    public SizePolicy getSizePolicy() {
        return SizePolicy.DEFAULT;
    }

    @Override
    public Rect getGeometry() {
        return geometry;
    }

    @Override
    public void setGeometry(Rect rect) {
        final Rect contents = new Rect(rect);
        contents.offset(getMargin());
        geometry.set(contents);
        update();
    }

    @Override
    public Optional<Layout> asLayout() {
        return Optional.of(this);
    }

    public void addWidget(Widget widget) {
        parent.addChild(widget);
        addItem(new WidgetItem(widget));
    }

    public void addLayout(Layout layout) {
        addItem(layout);
    }

    public abstract void addItem(LayoutItem item);

//    protected abstract void getItem(LayoutItem item);

    public abstract void removeItem(LayoutItem item);

    public abstract int countItems();
}
