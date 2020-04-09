package msifeed.mc.mellow.widgets.button;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.Label;

public class ButtonMultiLabel extends Button {
    private Part normalPart = Mellow.getPart("button_normal");
    private Part hoverPart = Mellow.getPart("button_hover");
    private Part pressPart = Mellow.getPart("button_press");
    private Part disabledPart = Mellow.getPart("button_disabled");

    protected Widget container = new Widget();

    public ButtonMultiLabel() {
        getMargin().set(3, 4, 6, 4);
        setLayout(new AnchorLayout());

        container.setZLevel(1);
        container.setLayout(ListLayout.VERTICAL);
        addChild(container);
    }

    public ButtonMultiLabel(String text) {
        this();
        addLabel(text);
    }

//    public String getLabels() {
//        return label.getText();
//    }

    public void addLabel(String text) {
        this.container.addChild(new Label(text));
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
