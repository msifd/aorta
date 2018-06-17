package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;

public class TextInput extends Widget {
    protected Part normalPart = Mellow.THEME.parts.get("sunken");
    protected Part focusedPart = Mellow.THEME.parts.get("sunken_focused");

    public TextInput() {

    }

    protected void renderBackground() {
        RenderParts.nineSlice(isFocused() ? focusedPart : normalPart, getGeometry());
    }
}
