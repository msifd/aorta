package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class VerticalLayout extends Layout {
    public static final VerticalLayout INSTANCE = new VerticalLayout(1);

    public final int spacing;

    public VerticalLayout(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public Point getSizeOfContent(Widget widget) {
        final Point size = new Point();

        final Collection<Widget> children = widget.getChildren();
        for (Widget child : children) {
            final Point sh = child.getLayoutSizeHint();
            if (sh.x > size.x)
                size.x = sh.x;
            size.y += sh.y;
//            size.y += spacing;
        }

        final Margins margin = widget.getMargin();
        size.x += margin.left + margin.right;
        size.y += margin.top + margin.bottom;
//        size.y += spacing * children.size();
//        if (!children.isEmpty())
//            size.y += spacing * (children.size() - 1);

        return size;
    }

    @Override
    public void apply(Widget widget, Collection<Widget> children) {
        final Geom geometry = new Geom(widget.getGeometry());
        final Margins margin = widget.getMargin();
        geometry.offsetPos(margin);
        geometry.offsetSize(margin);

        final int averageTargetChildHeight = geometry.h / children.size();
        int fixedHeight = 0;
        int fixedChildren = 0;
        for (Widget child : children) {
            final int preferred = getPreferredHeight(averageTargetChildHeight, child);
            if (preferred != averageTargetChildHeight) {
                fixedHeight += preferred;
                fixedChildren++;
            }
        }
        final int freeChildren = Math.max(1, children.size() - fixedChildren);
        final int targetChildHeight = (geometry.h - fixedHeight) / freeChildren;

        int yPos = 0;
        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            final int width = getPreferredWidth(geometry.w, child);
            final int height = getPreferredHeight(targetChildHeight, child);

            childGeom.set(geometry);
            childGeom.translate(0, yPos, child.getZLevel());
            childGeom.translate(child.getPos());
            childGeom.setSize(width, height);
            child.setDirty();

            yPos += childGeom.h;
            yPos += spacing;
        }
    }
}
