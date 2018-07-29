package mellow;

import msifeed.mc.mellow.layout.FreeLayout;
import msifeed.mc.mellow.layout.VerticalLayout;
import msifeed.mc.mellow.widgets.Widget;
import org.junit.Test;

public class TestNestedLayout extends LayoutFixture {
    @Test
    public void floatFloat() {
        scene.setLayout(new FreeLayout());
        container.setLayout(new FreeLayout());
        container.setPos(10, 10);
        container.setSizeHint(100, 100);
        checkGeom(container, 10, 10, 100, 100);

        givenWidgets(1);

        final Widget widget = widgets.get(0);
        widget.setPos(10, 10);
        widget.setSizeHint(50, 50);
        checkGeom(widget, 20, 20, 50, 50);
    }

    @Test
    public void floatFloatFloat() {
        scene.setLayout(new FreeLayout());
        container.setLayout(new FreeLayout());
        container.setPos(10, 10);
        container.setSizeHint(100, 100);
        checkGeom(container, 10, 10, 100, 100);

        givenWidgets(1);

        final Widget widget = widgets.get(0);
        widget.setPos(10, 10);
        widget.setSizeHint(50, 50);
        checkGeom(widget, 20, 20, 50, 50);
    }

    @Test
    public void floatVertical() {
        final int spacing = 1;

        scene.setLayout(new FreeLayout());
        scene.setSizeHint(500, 500);

        container.setLayout(new VerticalLayout(spacing));
        container.setPos(10, 10);
        container.setSizeHint(100, 100);

        checkGeom(container, 10, 10, 100, 100);

        givenWidgets(3);

        final int containerPos = container.getPos().x;
        final int spacings = spacing * (widgets.size() - 1);
        final int expWidth = container.getSizeHint().x;
        final int avgHeight = (container.getSizeHint().y - spacings) / widgets.size();

        int yPos = containerPos;
        for (Widget w : widgets) {
            checkGeom(w, containerPos, yPos, expWidth, avgHeight);
            yPos += avgHeight + spacing;
        }
    }
}
