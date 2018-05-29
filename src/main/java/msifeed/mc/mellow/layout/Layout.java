package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.widgets.Widget;

public abstract class Layout {
    protected Widget host;

    public void bind(Widget widget) {
        this.host = widget;
    }

    public abstract void update();
}
