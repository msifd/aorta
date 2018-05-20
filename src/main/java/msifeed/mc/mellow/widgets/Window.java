package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.render.RenderParts;

public class Window extends Widget {
    public Window() {
        pos.set(10, 10, 0);
        size.set(200, 100);
    }

    @Override
    public String getPartName() {
        return "window";
    }

    @Override
    public void renderSelf() {
        RenderParts.ninePatches(part, pos, size);
    }
}
