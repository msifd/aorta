package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Margins;
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
        final Geom geometry = new Geom(widget.getGeometry());
        geometry.offsetPos(margin);
//        geometry.offsetSize(margin);

        final Widget child = children.iterator().next();

        final Geom childGeom = child.getGeometry();
        childGeom.set(geometry);
        childGeom.setSize(child.getSizeHint());
        childGeom.translate(child.getPos(), child.getZLevel());

        switch (verAnchor) {
            case CENTER:
                childGeom.y += (geometry.h - childGeom.h) / 2;
                break;
            case TOP:
                break;
            default:
                break;
        }

        switch (horAnchor) {
            case CENTER:
                childGeom.x += (geometry.w - childGeom.w) / 2;
                break;
            case LEFT:
                break;
            default:
                break;
        }

        child.setDirty();
    }

    public enum Anchor {
        CENTER, TOP, LEFT
    }
}
