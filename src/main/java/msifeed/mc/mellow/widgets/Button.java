package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.utils.Rect;

public class Button extends Widget implements MouseHandler.Click {
    protected Part normalPart = Mellow.THEME.parts.get("button_normal");
    protected Label label = new Label(this);
    protected Runnable clickCallback = null;

    public Button(Widget parent) {
        this(parent, "");
    }

    public Button(Widget parent, String text) {
        super(parent);
        setMinSize(50, 10);
        setLabel(text);
        addChild(this.label);
    }

    public void setLabel(String text) {
        this.label.setText(text);
    }

    public void setClickCallback(Runnable callback) {
        this.clickCallback = callback;
    }

    @Override
    protected void updateSelf() {
        final Rect b = getBounds();
        label.setMargin(b.x + 2, b.y + (b.h - label.getBounds().y) / 2);
    }

    @Override
    protected void renderSelf() {
        renderBackground();
        renderLabel();
    }

    protected void renderLabel() {
        RenderWidgets.cropped(label, getBounds());
    }

    protected void renderBackground() {
        RenderParts.nineSlice(normalPart, getBounds());
    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {
        if (clickCallback != null)
            clickCallback.run();
    }
}
