package msifeed.mc.mellow.widgets.tabs;

import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;

public class TabArea extends Widget {
    private TabBar tabBar = new TabBar(this);
    private Widget contentWrap = new Widget();

    public TabArea() {
        setLayout(ListLayout.VERTICAL);

        tabBar.setZLevel(10);

        contentWrap.setLayout(new AnchorLayout());

        addChild(tabBar);
        addChild(contentWrap);
    }

    @Override
    public void clearChildren() {
        tabBar.clearChildren();
        contentWrap.clearChildren();
    }

    public void addTab(String name, Widget tabContent) {
        tabContent.setVisible(false);

        tabBar.addTab(name);
        contentWrap.addChild(tabContent);

        if (contentWrap.getChildren().size() == 1) {
            selectTab(0);
        }
    }

    public int getCurrentTabIndex() {
        return tabBar.getCurrentIndex();
    }

    public void selectTab(int index) {
        tabBar.selectTab(Math.max(0, index));
    }

    void selectContent(int i) {
        int j = 0;
        for (Widget w : contentWrap.getChildren()) {
            w.setVisible(i == j);
            j++;
        }
        contentWrap.getChildren().stream()
                .skip(i)
                .findFirst()
                .ifPresent(w -> w.setVisible(true));
    }
}
