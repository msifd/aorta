package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.utils.Point;

import java.util.Optional;

public class Scene extends Widget {

    public Scene() {
        setLayout(new AnchorLayout(AnchorLayout.Anchor.CENTER));
    }

    public Optional<Widget> lookupWidget(Point p) {
        Optional<Widget> prevWidget = childAt(p);
        while (prevWidget.isPresent()) {
            final Optional<Widget> nextWidget = prevWidget.get().childAt(p);
            if (nextWidget.isPresent())
                prevWidget = nextWidget;
            else
                break;
        }
        return prevWidget;
    }
}
