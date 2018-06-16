package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class AnchorLayout extends Layout {
    protected Anchor horAnchor;
    protected Anchor verAnchor;

    public AnchorLayout(Anchor both) {
        this.horAnchor = both;
        this.verAnchor = both;
    }

    public AnchorLayout(Anchor hor, Anchor ver) {
        this.horAnchor = hor;
        this.verAnchor = ver;
    }

    @Override
    public void apply(Widget widget, Collection<Widget> children) {
        if (children.isEmpty())
            return;

        final Margins margin = widget.getMargin();
        final Rect geometry = new Rect(widget.getGeometry());
        geometry.offsetPos(margin);
//        geometry.offsetSize(margin);

        final Widget item = children.iterator().next();

        final Rect itemGeom = new Rect();
        itemGeom.translate(geometry);
        itemGeom.translate(item.getPos());
        itemGeom.setSize(item.getSizeHint());
//        itemGeom.offsetSize(margin);

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
