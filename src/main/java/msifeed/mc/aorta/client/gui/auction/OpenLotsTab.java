package msifeed.mc.aorta.client.gui.auction;

import msifeed.mc.aorta.economy.auction.AuctionRpc;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.text.TextInput;
import net.minecraft.client.Minecraft;

public class OpenLotsTab extends Widget {
    private final OpenLotsBrowser list = new OpenLotsBrowser();
    private final TextInput bidInput = new TextInput();
    private final ButtonLabel bidBtn = new ButtonLabel("Bid");
    private final ButtonLabel buyoutBtn = new ButtonLabel("Buyout");

    OpenLotsTab() {
        setLayout(ListLayout.VERTICAL);

        addChild(list);

        // //

        final Widget bidFooter = new Widget();
        bidFooter.setLayout(ListLayout.HORIZONTAL);
        addChild(bidFooter);

        bidInput.setSizeHint(50, 0);
        bidInput.setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.PREFERRED);
        bidInput.setPlaceholderText("Bid");
        bidInput.setFilter(TextInput::isUnsignedInt);
        bidFooter.addChild(bidInput);

        bidBtn.setClickCallback(() -> {
            AuctionRpc.bidLot(list.selectedLot.id, bidInput.getInt());
        });
        bidFooter.addChild(bidBtn);

        buyoutBtn.setClickCallback(() -> {
            AuctionRpc.buyoutLot(list.selectedLot.id);
        });
        bidFooter.addChild(buyoutBtn);
    }

    @Override
    protected void updateSelf() {
        final String myName = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
        final boolean noLot = list.selectedLot == null;
//        final boolean myLot = !noLot && data.selectedLot.seller.equals(myName);
        final boolean myLot = false; // TODO: remove this
        bidBtn.setDisabled(noLot || myLot || bidInput.getInt() <= list.selectedLot.bid);
        buyoutBtn.setDisabled(noLot || myLot);
    }

    private class OpenLotsBrowser extends LotsBrowserWidget {
        @Override
        protected void updateLotsRequest() {
            AuctionRpc.getOpenLots("", this::onLotsUpdate);
        }
    }
}
