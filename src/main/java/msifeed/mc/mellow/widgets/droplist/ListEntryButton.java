package msifeed.mc.mellow.widgets.droplist;

import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.widgets.button.ButtonLabel;

class ListEntryButton extends ButtonLabel {
    private final int itemN;

    ListEntryButton(DropList parent, int n) {
        this.itemN = n;
        getMargin().set(1, 2, 1, 2);
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
