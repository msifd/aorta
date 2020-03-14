package msifeed.mc.mellow.widgets.scene;

import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Scene extends Widget {

    public Scene() {
        setLayout(new AnchorLayout());
    }

    public Stream<Widget> lookupWidget(Point p) {
        final List<Widget> active = new ArrayList<>();
        final List<Widget> pending = getLookupChildren().collect(Collectors.toList());
        final List<Widget> nextPending = new ArrayList<>();

        while (!pending.isEmpty()) {
            for (Widget pw : pending)
                pw.getLookupChildren().filter(Widget::isVisible).forEach(nextPending::add);
            active.addAll(pending);
            pending.clear();
            pending.addAll(nextPending);
            nextPending.clear();
        }

        return active
                .stream()
                .filter(widget -> {
                    final Widget parent = widget.getParent();
                    return parent == null || parent.containsAnyChildren(p);
                })
                .filter(widget -> widget.containsPoint(p))
                .sorted(Widget::compareDepth);
    }
}
