package msifeed.mc.mellow.widgets;

import com.google.common.eventbus.Subscribe;
import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.Widget;
import msifeed.mc.mellow.events.MouseEvent;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;

import java.util.function.Consumer;

public class Button extends Widget {
    protected Part normalPart = Mellow.THEME.parts.get("button_normal");
    protected Label label = new Label(this);
    protected Consumer<MouseEvent.Click> clickCallback = null;

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

    public void setClickCallback(Consumer<MouseEvent.Click> callback) {
        this.clickCallback = callback;
    }

    @Override
    protected void renderSelf() {
        renderBackground();
        renderLabel();
    }

    protected void renderLabel() {
        label.setPos(2, (size.y - label.size.y) / 2 + 1);
        RenderWidgets.cropped(label, getAbsPos(), size);
    }

    protected void renderBackground() {
        RenderParts.nineSlice(normalPart, getAbsPos(), size);
    }

    @Subscribe
    public void onClickEvent(MouseEvent.Click event) {
        if (event.target == this && clickCallback != null)
            clickCallback.accept(event);
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
