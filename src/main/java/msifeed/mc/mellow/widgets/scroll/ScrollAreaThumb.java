package msifeed.mc.mellow.widgets.scroll;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.widgets.button.Button;

class ScrollAreaThumb extends Button {
    private Part thumbPart = Mellow.THEME.parts.get("scrollbar_thumb");
    private Part thumbHoverPart = Mellow.THEME.parts.get("scrollbar_thumb_hover");

    ScrollAreaThumb() {
        setSizeHint(thumbPart.size);
    }

    @Override
    protected void renderSelf() {
        if (isHovered())
            RenderParts.nineSlice(thumbHoverPart, getGeometry());
        else
            RenderParts.nineSlice(thumbPart, getGeometry());
    }
}
