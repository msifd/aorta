package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Optional;

public abstract class LayoutItem {
    public abstract Point getPos();

    public abstract Point getSizeHint();

    public abstract SizePolicy getSizePolicy();

    public abstract Rect getGeometry();

    public abstract void setGeometry(Rect rect);

    public abstract void update();

    public Optional<Layout> asLayout() {
        return Optional.empty();
    }

    public Optional<Widget> asWidget() {
        return Optional.empty();
    }
}
