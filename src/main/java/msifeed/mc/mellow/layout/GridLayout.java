package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class GridLayout implements Layout {
    private int spacing = 1;

    public GridLayout() {
    }

    public GridLayout(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public Point layoutIndependent(Collection<Widget> children) {
        final Point contentSize = new Point();

        boolean labelWidget = true;

        int maxLabelWidth = 0;
        for (Widget child : children) {
            if (labelWidget)
                maxLabelWidth = Math.max(maxLabelWidth, child.getSizeHint().x);
            labelWidget = !labelWidget;
        }

        labelWidget = true;
        int lineHeight = 0;

        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.reset();
            childGeom.setSize(child.getSizeHint());
            childGeom.translate(child.getPos(), child.getZLevel());
            childGeom.translate(0, contentSize.y);
            child.setDirty();

            lineHeight = Math.max(lineHeight, childGeom.h);
            contentSize.x = Math.max(contentSize.x, childGeom.w);

            if (!labelWidget) {
                childGeom.translate(maxLabelWidth + spacing, 0);

                contentSize.y += lineHeight + spacing;
                lineHeight = 0;
            }

            labelWidget = !labelWidget;
        }

        return contentSize;
    }
}
