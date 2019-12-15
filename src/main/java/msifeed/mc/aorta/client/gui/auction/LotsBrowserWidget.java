package msifeed.mc.aorta.client.gui.auction;

import msifeed.mc.aorta.economy.auction.Lot;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.text.Label;

import java.util.ArrayList;

abstract class LotsBrowserWidget extends Widget {
    public final Widget lotsList = new Widget();

    public Lot selectedLot = null;
    public boolean updatingLots = true;

    LotsBrowserWidget() {
        setLayout(ListLayout.VERTICAL);
        setSizeHint(0, 50);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);

        // //

        final Widget searchHeader = new Widget();
        searchHeader.setLayout(ListLayout.HORIZONTAL);
        addChild(searchHeader);

        final ButtonLabel updateBtn = new ButtonLabel("Update");
        updateBtn.setClickCallback(this::callLotUpdate);
        searchHeader.addChild(updateBtn);

        // //

        lotsList.setLayout(ListLayout.VERTICAL);
        addChild(lotsList);
    }

    @Override
    protected void renderSelf() {
        RenderShapes.frame(getGeometry(), 1, 0);
    }

    private void callLotUpdate() {
//        if (updatingLots) return;

        updatingLots = true;
        lotsList.clearChildren();
        lotsList.addChild(new Label("Updating..."));

        selectedLot = null;
        updateLotsRequest();
    }

    protected void onLotsUpdate(ArrayList<Lot> selection) {
        lotsList.clearChildren();
        for (Lot lot : selection)
            lotsList.addChild(new BrowserLotView(lot));
        updatingLots = false;
    }

    protected abstract void updateLotsRequest();

    protected class BrowserLotView extends LotView {
        BrowserLotView(Lot lot) {
            super(lot);
        }

        @Override
        public boolean isSelected() {
            return selectedLot != null && selectedLot.id == this.lot.id;
        }

        @Override
        public void onClick(int xMouse, int yMouse, int button) {
            selectedLot = this.lot;
        }
    }
}
