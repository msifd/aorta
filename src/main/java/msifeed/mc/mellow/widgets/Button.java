package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;

public class Button extends Widget {
    private Part normalPart = Mellow.THEME.parts.get("button_normal");

    public Button(Widget parent) {
        super(parent);
    }

    @Override
    public void renderSelf() {
        RenderParts.ninePatches(normalPart, pos, size);
    }
}
