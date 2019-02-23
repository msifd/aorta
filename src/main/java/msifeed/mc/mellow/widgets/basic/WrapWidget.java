package msifeed.mc.mellow.widgets.basic;

import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.widgets.Widget;

public class WrapWidget extends Widget {
    public WrapWidget(Widget content) {
        setLayout(new AnchorLayout());
        addChild(content);
    }
}
