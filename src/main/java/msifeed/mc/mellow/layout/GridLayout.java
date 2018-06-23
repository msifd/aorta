package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class GridLayout extends Layout {
    public int spacing = 1;

    @Override
    public Point getSizeOfContent(Widget widget) {
        Point size = new Point(widget.getSizeHint());
        size.y = 0;

        boolean valueCol = false;
        for (Widget child : widget.getChildren()) {
            if (valueCol)
                size.y += child.getLayoutSizeHint().y + spacing;
            valueCol = !valueCol;
        }

        return size;
    }

    @Override
    public void apply(Widget widget, Collection<Widget> children) {
        int heightAcc = makeGrid(widget, children);

        final Point size = widget.getSizeHint();
        if (size.y != heightAcc) {
            size.y = heightAcc;
            widget.setDirty();
            makeGrid(widget, children);
        }
    }

    private int makeGrid(Widget widget, Collection<Widget> children) {
        final Margins margin = widget.getMargin();
        final Geom geometry = new Geom(widget.getGeometry());
        geometry.offsetPos(margin);
        geometry.offsetSize(margin);

        boolean labelCol = true;

        int minLabelWidth = 0;
        for (Widget child : children) {
            int minWidth = child.getSizePolicy().horizontalPolicy.canGrow
                    ? child.getSizeHint().x
                    : 0;
            if (labelCol && minWidth > minLabelWidth)
                minLabelWidth = minWidth;
            labelCol = !labelCol;
        }

        int halfGeomWidth = geometry.w / 2 - spacing;
        int labelWidth = Math.max(minLabelWidth, halfGeomWidth);
        int valueWidth = geometry.w - labelWidth - spacing;

        labelCol = true;
        int heightAcc = 0;
        for (Widget child : children) {
            final Point sh = child.getLayoutSizeHint();
            final Geom childGeom = child.getGeometry();
            final int width = labelCol ? labelWidth : valueWidth;

            childGeom.set(geometry);
            childGeom.setSize(width, sh.y);
            childGeom.translate(0, heightAcc, child.getZLevel());
            if (!labelCol)
                childGeom.translate(labelWidth + spacing, 0);
            child.setDirty();

            if (!labelCol) {
                heightAcc += childGeom.h;
                heightAcc += spacing;
            }
            labelCol = !labelCol;
        }

        return heightAcc;
    }
}
