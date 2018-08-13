package mellow;

import msifeed.mc.mellow.layout.FreeLayout;
import msifeed.mc.mellow.layout.VerticalLayout;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import org.junit.Test;

public class TestNestedLayout extends LayoutFixture {
    @Test
    public void freeFree() {
        final Point cPos = new Point(10, 10);
        final Point cSize = new Point(100, 100);
        final Point wPos = new Point(10, 10);
        final Point wSize = new Point(50, 50);

        container.setLayout(new FreeLayout());
        container.setPos(cPos);
        container.setSizeHint(cSize);
        checkGeom(container, cPos.x, cPos.y, 0, 0);

        givenWidgets(1);

        final Widget widget = widgets.get(0);
        widget.setPos(wPos);
        widget.setSizeHint(wSize);
        widget.setSizePolicy(SizePolicy.FIXED);
        checkGeom(widget, cPos.x + wPos.x, cPos.y + wPos.y, wSize.x, wSize.y);
    }

    @Test
    public void freeVertical() {
        final int cSpacing = 1;
        final Point cPos = new Point(10, 10);
        final Point wSize = new Point(50, 50);

        container.setLayout(new VerticalLayout(cSpacing));
        container.setPos(cPos);

        checkGeom(container, 10, 10, 0, 0);

        givenWidgets(3);

        for (Widget widget : widgets) {
            widget.setSizeHint(wSize);
            widget.setSizePolicy(SizePolicy.FIXED);
        }

        int yPos = cPos.y;
        for (Widget w : widgets) {
            checkGeom(w, cPos.x, yPos, wSize.x, wSize.y);
            yPos += wSize.y + cSpacing;
        }
    }
}
