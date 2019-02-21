package msifeed.mc.mellow.widgets.tabs;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.widgets.button.ButtonLabel;

class TabButton extends ButtonLabel {
    private Part inactivePart = Mellow.getPart("tab_inactive");
    private Part activePart = Mellow.getPart("tab_active");

    private final TabBar parent;

    TabButton(TabBar parent, String name) {
        super(name);
        this.parent = parent;

        getMargin().set(3, 4, 3, 4);
//        setSizePolicy(SizePolicy.Policy.MAXIMUM, SizePolicy.Policy.PREFERRED);
    }

    boolean isActive() {
        return parent.isActiveTab(this);
    }

    @Override
    protected void renderSelf() {
        if (isActive() || isHovered())
            RenderParts.nineSlice(activePart, getGeometry());
        else
            RenderParts.nineSlice(inactivePart, getGeometry());
    }
}
