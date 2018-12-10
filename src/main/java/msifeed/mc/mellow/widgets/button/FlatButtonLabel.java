package msifeed.mc.mellow.widgets.button;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;

public class FlatButtonLabel extends ButtonLabel {
    private Part flatNormalPart = Mellow.getPart("flat_button_normal");
    private Part flatHoverPart = Mellow.getPart("flat_button_hover");

    public FlatButtonLabel() {
        super();
        getMargin().set(1, 2, 2, 2);
//        setLayout(new AnchorLayout(AnchorLayout.Anchor.LEFT, AnchorLayout.Anchor.CENTER));
//
//        label.setZLevel(1);
//        addChild(label);
    }

    @Override
    protected void renderSelf() {
        if (isHovered())
            RenderParts.nineSlice(flatHoverPart, getGeometry());
//        else
//            RenderParts.nineSlice(flatNormalPart, getGeometry());
    }
}
