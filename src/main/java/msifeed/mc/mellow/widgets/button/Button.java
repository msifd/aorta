package msifeed.mc.mellow.widgets.button;

import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;
import java.util.Collections;

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
    public Collection<Widget> getLookupChildren() {
        return Collections.emptyList();
    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {
        if (!disabled && clickCallback != null)
            clickCallback.run();
    }
}
