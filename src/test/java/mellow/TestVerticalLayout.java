package mellow;

import msifeed.mc.mellow.layout.VerticalLayout;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TestVerticalLayout extends LayoutFixture {
    @Override
    public void init() {
        super.init();
        container.setLayout(new VerticalLayout(1));
        container.setPos(10, 10);
        container.setSizeHint(100, 100);
    }

    @Override
    public void initChild(Widget child) {
        child.setSizeHint(10, 10);
        child.setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
    }

    @Test
    public void singlePreferred() {
        givenWidgets(1);
        checkGeom(0, 10, 10, 100, 100);
    }

    @Test
    public void singlePreferredMoved() {
        givenWidgets(1);
        widgets.get(0).setPos(10, 10);
        checkGeom(0, 10, 10, 100, 100);
    }

    @Test
    public void singleFixed() {
        givenWidgets(1);
        widgets.get(0).setSizePolicy(SizePolicy.Policy.FIXED, SizePolicy.Policy.FIXED);
        checkGeom(0, 10, 10, 10, 10);
    }

    @Test
    public void twoPreferred() {
        givenWidgets(2);
        checkGeom(0, 10, 10, 100, 49);
        checkGeom(1, 10, 60, 100, 49);
    }

    @Test
    public void twoPreferredAndFixed() {
        givenWidgets(2);
        widgets.get(1).setSizePolicy(SizePolicy.Policy.FIXED, SizePolicy.Policy.FIXED);
        checkGeom(0, 10, 10, 100, 89);
        checkGeom(1, 10, 100, 10, 10);
    }
}
