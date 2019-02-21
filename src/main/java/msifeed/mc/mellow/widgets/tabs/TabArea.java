package msifeed.mc.mellow.widgets.tabs;

import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;

import java.util.ArrayList;

public class TabArea extends Widget {
    private TabBar tabBar = new TabBar(this);
    private ArrayList<Widget> contents = new ArrayList<>();
    private Widget contentWrap = new Widget();

    public TabArea() {
        setLayout(ListLayout.VERTICAL);
        contentWrap.setLayout(new AnchorLayout());

        addChild(tabBar);
        addChild(contentWrap);
    }

    public void addTab(String name, Widget tabContent) {
        tabBar.addTab(name);
        contents.add(tabContent);

        if (contents.size() == 1)
            tabBar.selectTab(0);
    }

    void selectContent(int i) {
        contentWrap.clearChildren();
        contentWrap.addChild(contents.get(i));
    }
}
