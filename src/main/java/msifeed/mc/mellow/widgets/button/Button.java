package msifeed.mc.mellow.widgets.button;

import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.widgets.Widget;

import java.util.stream.Stream;

public abstract class Button extends Widget implements MouseHandler.Click {
    private boolean disabled = false;
    private Runnable clickCallback = null;

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setClickCallback(Runnable callback) {
        this.clickCallback = callback;
    }

    @Override
    public Stream<Widget> getLookupChildren() {
        return Stream.empty();
    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {
        if (!disabled && clickCallback != null)
            clickCallback.run();
    }
}
