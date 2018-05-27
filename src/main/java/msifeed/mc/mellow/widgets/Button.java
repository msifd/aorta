package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.handlers.MouseHandler;

public class Button extends Widget implements MouseHandler.Click {
    protected Part normalPart = Mellow.THEME.parts.get("button_normal");
    protected Label label = new Label(this);
    protected Runnable clickCallback = null;

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

    public void setClickCallback(Runnable callback) {
        this.clickCallback = callback;
    }

    @Override
    protected void renderSelf() {
        renderBackground();
        renderLabel();
    }

    protected void renderLabel() {
        label.setPos(2, (size.y - label.size.y) / 2);
        RenderWidgets.cropped(label, getAbsPos(), size);
    }

    protected void renderBackground() {
        RenderParts.nineSlice(normalPart, getAbsPos(), size);
    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {
        if (clickCallback != null)
            clickCallback.run();
    }
}
