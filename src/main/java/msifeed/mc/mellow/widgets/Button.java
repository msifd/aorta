package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;

import java.util.Collection;
import java.util.Collections;

public class Button extends Widget implements MouseHandler.Click {
    protected Part normalPart = Mellow.THEME.parts.get("button_normal");
    protected Part hoverPart = Mellow.THEME.parts.get("button_hover");
    protected Part pressPart = Mellow.THEME.parts.get("button_press");

    protected Widget label;
    protected Runnable clickCallback = null;

    public Button() {
        this("");
    }

    public Button(String text) {
        this(new Label(text));
    }

    public Button(Widget label) {
        setSizeHint(50, 10);
        setLayout(new AnchorLayout(AnchorLayout.Anchor.CENTER));

        this.label = label;
        this.label.setZLevel(Math.max(1, label.getZLevel()));

        addChild(label);
    }

    public void setLabel(String text) {
        if (label instanceof Label)
            ((Label) this.label).setText(text);
    }

    public void setClickCallback(Runnable callback) {
        this.clickCallback = callback;
    }

    @Override
    protected void renderSelf() {
        if (isPressed())
            RenderParts.nineSlice(pressPart, getGeometry());
        else if (isHovered())
            RenderParts.nineSlice(hoverPart, getGeometry());
        else
            RenderParts.nineSlice(normalPart, getGeometry());
    }

    @Override
    protected void renderChildren() {
        RenderWidgets.beginCropped(getGeometry());
        super.renderChildren();
        RenderWidgets.endCropped();
    }

    @Override
    protected Collection<Widget> getLookupChildren() {
        return Collections.emptyList();
    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {
        if (clickCallback != null)
            clickCallback.run();
    }

    public static class AlmostTransparentButton extends Button {
        @Override
        protected void renderSelf() {
            if (isHovered())
                RenderShapes.rect(getGeometry(), 0x997577, 80);
        }
    }
}
