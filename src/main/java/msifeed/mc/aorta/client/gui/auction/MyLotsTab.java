package msifeed.mc.aorta.client.gui.auction;

import msifeed.mc.aorta.economy.auction.AuctionRpc;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;

public class MyLotsTab extends Widget {
    private final MyLotsBrowser list = new MyLotsBrowser();
    private final ButtonLabel redeemBtn = new ButtonLabel("Redeem");

    MyLotsTab() {
        setLayout(ListLayout.VERTICAL);

        // //

//        final Widget searchHeader = new Widget();
//        searchHeader.setLayout(ListLayout.HORIZONTAL);
//        addChild(searchHeader);


        // //

        addChild(list);

        // //

        final Widget bidFooter = new Widget();
        bidFooter.setLayout(ListLayout.HORIZONTAL);
        addChild(bidFooter);

        redeemBtn.setClickCallback(() -> {
            AuctionRpc.redeemLot(list.selectedLot.id);
        });
        bidFooter.addChild(redeemBtn);
    }

    private class MyLotsBrowser extends LotsBrowserWidget {
        @Override
        protected void updateLotsRequest() {
            AuctionRpc.getMyLots(this::onLotsUpdate);
        }
    }
}
