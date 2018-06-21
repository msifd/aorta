package msifeed.mc.mellow.widgets;

import com.google.common.collect.ImmutableList;
import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Point;

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
    }

    public void setLabel(String text) {
        if (label instanceof Label)
            ((Label) this.label).setText(text);
    }

    public void setClickCallback(Runnable callback) {
        this.clickCallback = callback;
    }

    @Override
    protected void updateLayout() {
        super.updateLayout();
        if (label != null)
            layout.apply(this, ImmutableList.of(label));
    }

    @Override
    protected void renderSelf() {
        renderBackground();
        renderLabel();
    }

    protected void renderLabel() {
        if (label != null)
            RenderWidgets.cropped(label, getGeometry());
    }

    protected void renderBackground() {
        if (isPressed())
            RenderParts.nineSlice(pressPart, getGeometry());
        else if (isHovered())
            RenderParts.nineSlice(hoverPart, getGeometry());
        else
            RenderParts.nineSlice(normalPart, getGeometry());
    }

    @Override
    public Collection<Widget> childrenAt(Point p) {
        return Collections.emptyList();
    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {
        if (clickCallback != null)
            clickCallback.run();
    }

    public static class ToggleableButton extends Button {
        protected boolean selected = false;

        public boolean isSelected() {
            return selected;
        }

        @Override
        public void onClick(int xMouse, int yMouse, int button) {
            selected = !selected;
            super.onClick(xMouse, yMouse, button);
        }
    }

    public static class AlmostTransparentButton extends Button {
        @Override
        protected void renderBackground() {
//            super.renderBackground();
            if (isHovered())
                RenderShapes.rect(getGeometry(), 0x997577, 80);
        }
    }
}
