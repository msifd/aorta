package mellow;

import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.FreeLayout;
import msifeed.mc.mellow.widgets.Widget;
import org.junit.Test;

public class TestSimpleLayout extends LayoutFixture {
    private Widget widget;

    @Override
    public void init() {
        super.init();
        givenWidgets(1);
        widget = widgets.get(0);
    }

    @Test
    public void floatLayout() {
        container.setLayout(new FreeLayout());
        container.setPos(10, 10);
        checkGeom(10, 10, 0, 0);

        widget.setPos(10, 10);
        checkGeom(20, 20, 0, 0);
    }

    @Test
    public void anchorLayoutTopLeft() {
        container.setLayout(new AnchorLayout(AnchorLayout.Anchor.LEFT, AnchorLayout.Anchor.TOP));
        container.setPos(10, 10);
        container.getSizeHint().set(30, 30);
        checkGeom(10, 10, 0, 0);

        widget.setPos(10, 10);
        widget.getSizeHint().set(10, 10);
        checkGeom(20, 20, 10, 10);
    }

    @Test
    public void anchorLayoutCenterLeft() {
        container.setLayout(new AnchorLayout(AnchorLayout.Anchor.LEFT, AnchorLayout.Anchor.CENTER));
        container.setPos(10, 10);
        container.getSizeHint().set(30, 30);
        checkGeom(10, 25, 0, 0);

        widget.setPos(10, 10);
        widget.getSizeHint().set(10, 10);
        checkGeom(20, 30, 10, 10);
    }

    @Test
    public void anchorLayoutCenter() {
        container.setLayout(new AnchorLayout());
        container.setPos(10, 10);
        container.getSizeHint().set(30, 30);
        checkGeom(25, 25, 0, 0);

        widget.setPos(10, 10);
        widget.getSizeHint().set(10, 10);
        checkGeom(30, 30, 10, 10);
    }

    private void checkGeom(int x, int y, int w, int h) {
        super.checkGeom(0, x, y, w, h);
    }
}
