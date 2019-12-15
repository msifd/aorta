package msifeed.mc.aorta.client.gui.auction;

import msifeed.mc.aorta.economy.auction.AuctionRpc;
import msifeed.mc.aorta.economy.auction.Lot;
import msifeed.mc.aorta.economy.auction.Prices;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class AddLotTab extends Widget {
    private static final int MAX_PAYLOAD = 5;

    private final TextInput durationInput = new TextInput();
    private final TextInput startBidInput = new TextInput();
    private final TextInput buyoutInput = new TextInput();
    private final Label depositFeeLbl = new Label();
    private final ButtonLabel publishBtn = new ButtonLabel("Publish");

    private final Widget payloadSlotsSection = new Widget();
    private final Widget invSlotsSection = new Widget();

    AddLotTab() {
        setLayout(ListLayout.VERTICAL);

        final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        final InventoryPlayer playerInventory = player.inventory;

        final Widget params = new Widget();
        params.setLayout(new GridLayout());
        addChild(params);

        params.addChild(new Label("Duration (days)"));
        durationInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 1, 10));
        params.addChild(durationInput);

        params.addChild(new Label("Start bid"));
        startBidInput.getSizeHint().x = 100;
        startBidInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 1, 1_000_000));
        params.addChild(startBidInput);

        params.addChild(new Label("Buyout price"));
        buyoutInput.getSizeHint().x = 100;
        buyoutInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, 1_000_001));
        params.addChild(buyoutInput);

        // //

        payloadSlotsSection.setLayout(ListLayout.HORIZONTAL);
        addChild(payloadSlotsSection);

        for (int i = 0; i < MAX_PAYLOAD; i++)
            payloadSlotsSection.addChild(new ItemStackSlot());

        payloadSlotsSection.addChild(depositFeeLbl);

        addChild(new Separator());

        // //

        invSlotsSection.setLayout(ListLayout.VERTICAL);
        addChild(invSlotsSection);

        for (int row = 0; row < 4; row++) {
            final Widget slotsRow = new Widget();
            slotsRow.setLayout(ListLayout.HORIZONTAL);
            invSlotsSection.addChild(slotsRow);

            for (int col = 0; col < 9; col++) {
                final ItemStackSlot slot = new ItemStackSlot();
                final ItemStack item = playerInventory.mainInventory[row * 9 + col];
                if (item != null)
                    slot.drop(new ItemStackWidget(item));
                slotsRow.addChild(slot);
            }
        }

        // //

        final Widget footer = new Widget();
        footer.setLayout(ListLayout.HORIZONTAL);
        addChild(footer);

        publishBtn.setDisabled(true);
        publishBtn.setClickCallback(() -> {
            final LocalDateTime expiration = LocalDateTime.now(ZoneOffset.UTC).plusDays(durationInput.getInt());

            final Lot l = new Lot();
            l.seller = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
            l.expiration = expiration;
            l.bid = startBidInput.getInt();
            l.buyoutPrice = buyoutInput.getInt();
            for (Widget slot : payloadSlotsSection.getChildren()) {
                if (!(slot instanceof ItemStackSlot)) continue;
                final ItemStackSlot s = (ItemStackSlot) slot;
                if (s.getItem() != null)
                    l.payload.add(s.getItem().itemStack);
            }

            AuctionRpc.publishLot(l);
//            data.update();
            reset();
        });
        footer.addChild(publishBtn);

        // //

        reset();
    }

    @Override
    protected void updateSelf() {
        final int duration = durationInput.getInt();
        final int bid = startBidInput.getInt();
        final int buyout = buyoutInput.getInt();

        final boolean isInputsOk = duration > 0 && duration <= 10
                && bid > 0
                && (buyout == 0 || buyout > bid)
                && payloadSlotsSection.getChildren().stream()
                .anyMatch(s -> s instanceof ItemStackSlot && ((ItemStackSlot) s).getItem() != null);
        publishBtn.setDisabled(!isInputsOk);

        final int depositFee = payloadSlotsSection.getChildren().stream()
                            .filter(w -> w instanceof ItemStackSlot)
                            .filter(w -> ((ItemStackSlot) w).getItem() != null)
                            .mapToInt(w -> Prices.getMSV(((ItemStackSlot) w).getItem().itemStack))
                            .sum();
        depositFeeLbl.setText("Deposit fee: " + depositFee);
    }

    private void reset() {
        durationInput.setText("");
        startBidInput.setText("");
        buyoutInput.setText("");

        for (Widget slot : payloadSlotsSection.getChildren())
            if (slot instanceof ItemStackSlot)
                ((ItemStackSlot) slot).release();
    }
}
