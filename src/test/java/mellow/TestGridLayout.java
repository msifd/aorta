package mellow;

import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import org.junit.Ignore;
import org.junit.Test;

public class TestGridLayout extends LayoutFixture {
    @Override
    public void init() {
        super.init();
        container.setLayout(new GridLayout(1));
        container.setPos(10, 10);
        container.setSizeHint(100, 100);
    }

    @Override
    void initChild(Widget child) {
        child.setSizeHint(10, 10);
    }

    @Ignore
    @Test
    public void twoPairs() {
        givenWidgets(4);
        checkGeom(0, 10, 10, 10, 10);
        checkGeom(1, 21, 10, 10, 10);
        checkGeom(2, 10, 21, 10, 10);
        checkGeom(3, 21, 21, 10, 10);
    }
}
