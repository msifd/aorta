package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.Widget;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;

public class Button extends Widget {
    protected Part normalPart = Mellow.THEME.parts.get("button_normal");
    protected Label label = new Label(this);

    public Button(Widget parent) {
        this(parent, "");
    }

    public Button(Widget parent, String label) {
        super(parent);
        setSize(50, 10);
        setLabel(label);
    }

    public void setLabel(String text) {
        this.label.setText(text);
    }

    @Override
    protected void renderSelf() {
        renderBackground();
        renderLabel();
    }

    protected void renderLabel() {
        label.setPos(1, (size.y - label.size.y) / 2 + 1);
        RenderWidgets.cropped(label, getAbsPos(), size);
    }

    protected void renderBackground() {
        RenderParts.nineSlice(normalPart, getAbsPos(), size);
    }

    public static class Transparent extends Button {
        public Transparent(Widget parent) {
            super(parent);
        }

        @Override
        protected void renderBackground() {
        }
    }
}
