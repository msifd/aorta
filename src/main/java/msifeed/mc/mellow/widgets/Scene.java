package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.layout.FloatLayout;
import msifeed.mc.mellow.utils.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class Scene extends Widget {

    public Scene() {
        super(null);
        setLayout(new FloatLayout());
    }

    public Optional<Widget> lookupWidget(Point p) {
        final ArrayList<Widget> list = new ArrayList<>();
        addChildrenAt(list, p);
        return list.stream().max(Comparator.comparingInt(Widget::getDepth));
    }
}
