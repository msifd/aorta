package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;

public class Window extends Widget {
    private Part part = Mellow.THEME.parts.get("window");
    private Label title = new Label(this);

    public Window(Widget parent) {
        super(parent);
        pos.set(10, 10, 0);
        size.set(200, 100);

        title.pos.set(3, 4, 0);
        title.text = "Title";
    }

    @Override
    public void renderSelf() {
        RenderParts.ninePatches(part, pos, size);
        title.render();
    }
}
