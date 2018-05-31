package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.Layout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Point;

import java.util.Collection;

public class Button extends Widget implements MouseHandler.Click {
    protected Part normalPart = Mellow.THEME.parts.get("button_normal");
    protected Part hoverPart = Mellow.THEME.parts.get("button_hover");
    protected Part pressPart = Mellow.THEME.parts.get("button_press");

    protected Label label = new Label(this);
    protected Runnable clickCallback = null;

    public Button(Widget parent) {
        this(parent, "");
    }

    public Button(Widget parent, String text) {
        super(parent);
        setMinSize(50, 10);
        padding.set(2);
        setLabel(text);
        setLayout(new AnchorLayout(AnchorLayout.Anchor.CENTER));

//        label.setMargin(2, 2);

        addChild(this.label);
    }

    private Button(Widget parent, Layout layout) {
        super(parent);
        setLayout(layout);
    }

    public void setLabel(String text) {
        this.label.setText(text);
    }

    public void setClickCallback(Runnable callback) {
        this.clickCallback = callback;
    }

//    @Override
//    protected void updateSelf() {
//        final Rect b = getBounds();
//    }

    @Override
    protected void renderSelf() {
        renderBackground();
        renderLabel();
    }

    protected void renderLabel() {
        RenderWidgets.cropped(label, getBounds());
    }

    protected void renderBackground() {
        if (isPressed())
            RenderParts.nineSlice(pressPart, getBounds());
        else if (isHovered())
            RenderParts.nineSlice(hoverPart, getBounds());
        else
            RenderParts.nineSlice(normalPart, getBounds());
    }

    @Override
    public void addChildrenAt(Collection<Widget> widgets, Point p) {
    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {
        if (clickCallback != null)
            clickCallback.run();
    }
}
