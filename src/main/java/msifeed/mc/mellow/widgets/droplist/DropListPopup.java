package msifeed.mc.mellow.widgets.droplist;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.layout.VerticalLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;

class DropListPopup extends Widget {
    private final Part listPart = Mellow.THEME.parts.get("dropdown_list");
    private final DropList parent;

    DropListPopup(DropList parent) {
        this.parent = parent;
        setPos(1, 0);
        getMargin().set(1, 1, 6, 1);
        setSizeHint(10, parent.getItems().size() * 11 + 2);
        setSizePolicy(SizePolicy.Policy.FIXED, SizePolicy.Policy.FIXED);
        setLayout(VerticalLayout.INSTANCE);

        for (int i = 0; i < parent.getItems().size(); i++) {
            final ListEntryButton entry = new ListEntryButton(parent, i);
            addChild(entry);
        }
    }

    @Override
    public Point getSizeHint() {
        final Point p = super.getSizeHint();
        p.x = parent.header.getTextBgWidth() - 2;
        return p;
    }

    @Override
    public boolean isVisible() {
        return parent.isOpened();
    }

    @Override
    protected void renderSelf() {
        RenderParts.nineSlice(listPart, getGeometry());
    }
}
