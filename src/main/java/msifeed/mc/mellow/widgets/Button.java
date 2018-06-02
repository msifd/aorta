package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.Layout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Point;

import java.util.Optional;

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
        setSizeHint(50, 10);
        setLabel(text);

        final AnchorLayout layout = new AnchorLayout(this, AnchorLayout.Anchor.CENTER);
        layout.getMargin().set(2, 2, 2, 2);
        layout.addWidget(label);
        setLayout(layout);
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

    @Override
    protected void renderSelf() {
        renderBackground();
        renderLabel();
    }

    protected void renderLabel() {
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
    public Optional<Widget> childAt(Point p) {
        return Optional.empty();
    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {
        if (clickCallback != null)
            clickCallback.run();
    }
}
