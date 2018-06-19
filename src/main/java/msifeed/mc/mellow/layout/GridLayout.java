package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class GridLayout extends Layout {
    public int spacing = 1;

    @Override
    public Point sizeHintOfContent(Widget widget) {
        Point size = new Point(widget.getSizeHint());
        size.y = 0;

        boolean valueCol = false;
        for (Widget child : widget.getChildren()) {
            if (valueCol)
                size.y += child.getSizeHint().y + spacing;
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
        final Rect geometry = new Rect(widget.getGeometry());
        geometry.offsetPos(margin);
        geometry.offsetSize(margin);

        boolean labelCol = true;

        int minLabelWidth = 0;
//        int minValueWidth = 0;
        for (Widget child : children) {
            int minWidth = child.getSizePolicy().horizontalPolicy == SizePolicy.Policy.MAXIMUM
                    ? child.getSizeHint().x
                    : 0;
            if (labelCol && minWidth > minLabelWidth)
                minLabelWidth = minWidth;
//            if (!labelCol && minWidth > minValueWidth)
//                minValueWidth = minWidth;
            labelCol = !labelCol;
        }

        int halfGeomWidth = geometry.w / 2 - spacing;
        int labelWidth = Math.max(minLabelWidth, halfGeomWidth);
        int valueWidth = geometry.w - labelWidth - spacing;

        labelCol = true;
        int heightAcc = 0;
        for (Widget child : children) {
            final Point sh = child.getSizeHint();
            final Rect itemGeom = new Rect();
            final int width = labelCol ? labelWidth : valueWidth;

            itemGeom.translate(geometry);
            itemGeom.translate(0, heightAcc);
            if (!labelCol) {
                itemGeom.translate(labelWidth + spacing, 0);
            }
            itemGeom.setSize(width, sh.y);

            child.setGeometry(itemGeom);

            if (!labelCol) {
                heightAcc += itemGeom.h;
                heightAcc += spacing;
            }

            labelCol = !labelCol;
        }

        return heightAcc;
    }
}
