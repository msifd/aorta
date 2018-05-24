package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.Widget;
import msifeed.mc.mellow.WidgetCollection;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;

public class Window extends WidgetCollection {
    private Part part = Mellow.THEME.parts.get("window");
    private Button.Transparent header = new Button.Transparent(this);

    public Window(Widget parent) {
        super(parent);
        setPos(10, 10, 0);
        setSize(200, 100);

        header.setPos(1, 1);
        header.setLabel("Title goes here");
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
}
