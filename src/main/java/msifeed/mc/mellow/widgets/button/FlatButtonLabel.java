package msifeed.mc.mellow.widgets.button;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;

public class FlatButtonLabel extends ButtonLabel {
    private Part flatHoverPart = Mellow.getPart("flat_button_hover");

    public FlatButtonLabel() {
        super();
        getMargin().set(1, 2, 2, 2);
    }

    public FlatButtonLabel(String label) {
        this();
        setLabel(label);
    }

    @Override
    protected void renderSelf() {
        if (!isDisabled() && isHovered())
            RenderParts.nineSlice(flatHoverPart, getGeometry());
    }
}
