package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.widgets.Widget;

public class AnchorLayout extends Layout {
    protected Anchor horAnchor;
    protected Anchor verAnchor;
    protected LayoutItem item;

    public AnchorLayout(Widget parent, Anchor both) {
        super(parent);
        this.horAnchor = both;
        this.verAnchor = both;
    }

    public AnchorLayout(Widget parent, Anchor hor, Anchor ver) {
        super(parent);
        this.horAnchor = hor;
        this.verAnchor = ver;
    }

    @Override
    public void addItem(LayoutItem item) {
        this.item = item;
    }

    @Override
    public void removeItem(LayoutItem item) {
        if (this.item == item)
            this.item = null;
    }

    @Override
    public int countItems() {
        return item == null ? 0 : 1;
    }

    @Override
    public void update() {
        if (item != null) {
            setAnchoredGeom();
        }
    }

    private void setAnchoredGeom() {
        final Rect itemGeom = new Rect();
        itemGeom.translate(geometry);
        itemGeom.translate(item.getPos());
        itemGeom.setSize(item.getSizeHint());

        switch (verAnchor) {
            case CENTER:
                itemGeom.y += (geometry.h - itemGeom.h) / 2;
                break;
            case TOP:
                break;
            default:
                break;
        }

        switch (horAnchor) {
            case CENTER:
                itemGeom.x += (geometry.w - itemGeom.w) / 2;
                break;
            case LEFT:
                break;
            default:
                break;
        }

        item.setGeometry(itemGeom);
    }

    public enum Anchor {
        CENTER, TOP, LEFT
    }
}
