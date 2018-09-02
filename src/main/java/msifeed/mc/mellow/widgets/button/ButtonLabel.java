package msifeed.mc.mellow.widgets.button;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.basic.Label;

public class ButtonLabel extends Button {
    private Part normalPart = Mellow.THEME.parts.get("button_normal");
    private Part hoverPart = Mellow.THEME.parts.get("button_hover");
    private Part pressPart = Mellow.THEME.parts.get("button_press");
    private Part disabledPart = Mellow.THEME.parts.get("button_disabled");

    protected Label label = new Label();

    public ButtonLabel() {
        getMargin().set(3, 3, 6, 3);
        setLayout(new AnchorLayout());

        label.setZLevel(Math.max(1, label.getZLevel()));

        addChild(label);
    }

    public ButtonLabel(String text) {
        this();
        label.setText(text);
    }

    public void setLabel(String text) {
        this.label.setText(text);
    }

    @Override
    protected void renderSelf() {
        if (isDisabled())
            RenderParts.nineSlice(disabledPart, getGeometry());
        else if (isPressed())
            RenderParts.nineSlice(pressPart, getGeometry());
        else if (isHovered())
            RenderParts.nineSlice(hoverPart, getGeometry());
        else
            RenderParts.nineSlice(normalPart, getGeometry());
    }
}
