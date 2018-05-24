package msifeed.mc.mellow.widgets;

import com.google.common.eventbus.EventBus;
import msifeed.mc.mellow.Widget;
import msifeed.mc.mellow.WidgetCollection;

import javax.vecmath.Point3f;

public class Scene extends WidgetCollection {
    private EventBus eventBus = new EventBus();

    public Scene() {
        super(null);
    }

    @Override
    public Point3f getAbsPos() {
        return pos;
    }

    @Override
    public Widget getTopWidget() {
        return this;
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }
}
