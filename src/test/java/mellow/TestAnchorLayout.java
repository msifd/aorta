package mellow;

import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.FreeLayout;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import org.junit.Test;

public class TestAnchorLayout extends LayoutFixture {
    private Widget widget;

    @Override
    public void init() {
        super.init();
        givenWidgets(1);
        widget = widgets.get(0);
    }

    @Test
    public void floatLayout() {
        final Point cPos = new Point(10, 10);
        final Point wPos = new Point(10, 10);
        final Point wSize = new Point(50, 50);

        container.setLayout(new FreeLayout());
        container.setPos(cPos);
        checkGeom(cPos.x, cPos.y, 0, 0);

        widget.setPos(wPos);
        widget.setSizeHint(wSize);
        widget.setSizePolicy(SizePolicy.FIXED);
        checkGeom(cPos.x + wPos.x, cPos.y + wPos.y, wSize.x, wSize.y);
    }

    @Test
    public void anchorLayoutTopLeft() {
        final Point cPos = new Point(10, 10);
        final Point wPos = new Point(10, 10);
        final Point wSize = new Point(50, 50);

        container.setLayout(new AnchorLayout(AnchorLayout.Anchor.LEFT, AnchorLayout.Anchor.TOP));
        container.setPos(cPos);
        checkGeom(cPos.x, cPos.y, 0, 0);

        widget.setPos(wPos);
        widget.setSizeHint(wSize);
        widget.setSizePolicy(SizePolicy.FIXED);
        checkGeom(cPos.x + wPos.x, cPos.y + wPos.y, wSize.x, wSize.y);
    }

    @Test
    public void anchorLayoutCenterLeft() {
        final Point cPos = new Point(10, 10);
        final Point cSize = new Point(100, 100);
        final Point wPos = new Point(10, 10);
        final Point wSize = new Point(50, 50);

        container.setLayout(new AnchorLayout(AnchorLayout.Anchor.LEFT, AnchorLayout.Anchor.CENTER));
        container.setPos(cPos);
        container.setSizeHint(cSize);
        container.setSizePolicy(SizePolicy.FIXED);
        checkGeom(container, cPos.x, cPos.y, cSize.x, cSize.y);

        widget.setPos(wPos);
        widget.setSizeHint(wSize);
        widget.setSizePolicy(SizePolicy.FIXED);
        checkGeom(widget, cPos.x + wPos.x, cPos.y + wPos.y + (cSize.y - wSize.y) / 2, wSize.x, wSize.y);
    }

    @Test
    public void anchorLayoutCenter() {
        final Point cPos = new Point(10, 10);
        final Point cSize = new Point(100, 100);
        final Point wPos = new Point(10, 10);
        final Point wSize = new Point(50, 50);

        container.setLayout(new AnchorLayout(AnchorLayout.Anchor.CENTER, AnchorLayout.Anchor.CENTER));
        container.setPos(cPos);
        container.setSizeHint(cSize);
        container.setSizePolicy(SizePolicy.FIXED);
        checkGeom(container, cPos.x, cPos.y, cSize.x, cSize.y);

        widget.setPos(wPos);
        widget.setSizeHint(wSize);
        widget.setSizePolicy(SizePolicy.FIXED);
        checkGeom(widget, cPos.x + wPos.x + (cSize.x - wSize.x) / 2, cPos.y + wPos.y + (cSize.y - wSize.y) / 2, wSize.x, wSize.y);
    }

    private void checkGeom(int x, int y, int w, int h) {
        checkGeom(widget, x, y, w, h);
    }
}
