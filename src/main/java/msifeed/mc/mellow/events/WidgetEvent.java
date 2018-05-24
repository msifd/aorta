package msifeed.mc.mellow.events;

import msifeed.mc.mellow.Widget;

public class WidgetEvent {
    public final Widget target;

    public WidgetEvent(Widget target) {
        this.target = target;
    }
}
