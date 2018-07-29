package mellow;

import msifeed.mc.mellow.layout.FreeLayout;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.scene.Scene;
import org.junit.Before;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public abstract class LayoutFixture {
    Scene scene;
    Widget container;
    ArrayList<Widget> widgets;

    @Before
    public void init() {
        scene = new Scene();
        container = new Widget();
        widgets = new ArrayList<>();
        scene.setLayout(new FreeLayout());
        scene.setSizeHint(1000, 1000);
        scene.addChild(container);
    }

    void givenWidgets(int count) {
        IntStream.range(0, count).forEach(i -> {
            final Widget w = new Widget();
            initChild(w);
            widgets.add(w);
        });
        addChildren();
    }

    void initChild(Widget child) {
    }

    void addChildren() {
        for (Widget w : widgets)
            container.addChild(w);
    }

    void checkGeom(int i, int x, int y, int w, int h) {
        checkGeom(widgets.get(i), x, y, w, h);
    }

    void checkGeom(Widget widget, int x, int y, int w, int h) {
        update();
        final Geom g = widget.getGeometry();
        assertEquals("X pos", x, g.x);
        assertEquals("Y pos", y, g.y);
        assertEquals("Width", w, g.w);
        assertEquals("Height", h, g.h);
    }

    void update() {
        for (Widget w : widgets)
            w.setDirty();
        scene.update();
    }
}
