package msifeed.mc.mellow.widgets.spoiler;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.widgets.button.ButtonLabel;

class SpoilerButton extends ButtonLabel {
    private Part closedPart = Mellow.getPart("spoiler_closed");
    private Part hoverPart = Mellow.getPart("spoiler_hover");
    private Part openPart = Mellow.getPart("spoiler_open");

    boolean open = false;

    SpoilerButton(String label) {
        super(label);
    }

    @Override
    protected void renderSelf() {
        if (open)
            RenderParts.nineSlice(openPart, getGeometry());
        else if (isHovered())
            RenderParts.nineSlice(hoverPart, getGeometry());
        else
            RenderParts.nineSlice(closedPart, getGeometry());
    }
}
