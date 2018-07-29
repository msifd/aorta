package msifeed.mc.mellow.widgets.droplist;

import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.widgets.button.Button;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.button.ButtonLabel;

class ListEntryButton extends ButtonLabel {
    private final int itemN;

    ListEntryButton(DropList parent, int n) {
        this.itemN = n;
        setPos(1, 0);
        setZLevel(2);
        setSizeHint(0, 11);
        getMargin().set(2, 0);
        setLayout(new AnchorLayout(AnchorLayout.Anchor.LEFT, AnchorLayout.Anchor.CENTER));

        setLabel(parent.getItems().get(n).toString());
        setClickCallback(() -> {
            parent.selectItem(itemN);
            parent.setOpened(false);
        });

        label.setColor(label.darkColor);
    }

    @Override
    protected void renderSelf() {
        if (isHovered())
            RenderShapes.rect(getGeometry(), 0xc3a38a, 255);
    }
}
