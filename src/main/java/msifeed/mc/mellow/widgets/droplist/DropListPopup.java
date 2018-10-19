package msifeed.mc.mellow.widgets.droplist;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

class DropListPopup extends Widget {
    private final Part listPart = Mellow.THEME.parts.get("dropdown_list");
    private final DropList parent;

    DropListPopup(DropList parent) {
        this.parent = parent;
        getMargin().set(1, 1, 2, 1);
        setLayout(ListLayout.VERTICAL);

        for (int i = 0; i < parent.getItems().size(); i++) {
            final ListEntryButton entry = new ListEntryButton(parent, i);
            addChild(entry);
        }
    }

    @Override
    public Point getPos() {
        return new Point(1, parent.getContentSize().y);
    }

    @Override
    public boolean isVisible() {
        return parent.isOpened();
    }

    @Override
    public void render() {
        RenderWidgets.toggleCropping();
        super.render();
        RenderWidgets.toggleCropping();
    }

    @Override
    protected void renderSelf() {
        RenderParts.nineSlice(listPart, getGeometry());
    }
}
