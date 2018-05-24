package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.Widget;
import msifeed.mc.mellow.WidgetCollection;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;

import javax.vecmath.Point3f;

public class Window extends WidgetCollection {
    private Part part = Mellow.THEME.parts.get("window");
    private Button.Transparent header = new Button.Transparent(this);

    public Window(Widget parent) {
        super(parent);
        setPos(10, 10, 0);
        setSize(200, 100);

        header.setPos(1, 1);
        header.setLabel("Title goes here");
        header.setClickCallback(event -> {
            System.out.println("My header just got clicked!");
        });
    }

    @Override
    public void setSize(float w, float h) {
        super.setSize(w, h);
        header.setSize(w - 2, 12);
    }

    @Override
    protected void renderSelf() {
        RenderParts.nineSlice(part, getAbsPos(), size);
        header.render();
    }

    @Override
    public Widget lookupWidget(Point3f p) {
        if (header.lookupWidget(p) != null)
            return header;
        return super.lookupWidget(p);
    }
}
