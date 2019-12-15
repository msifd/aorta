package msifeed.mc.aorta.client.gui.auction;

import msifeed.mc.aorta.economy.auction.Lot;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.Label;
import net.minecraft.item.ItemStack;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

abstract class LotView extends Widget implements MouseHandler.Click {
    protected final Lot lot;

    LotView(Lot lot) {
        this.lot = lot;

        setLayout(ListLayout.HORIZONTAL);
//        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
//        setSizeHint(100, 30);
        getMargin().set(2);

        // //

        if (lot.hasExpiration()) {
            final LocalDateTime ldt = LocalDateTime.now(ZoneOffset.UTC);
            final long days = ldt.until(lot.expiration, ChronoUnit.DAYS);
            final long hours = ldt.until(lot.expiration, ChronoUnit.HOURS);
            final String until = days > 0
                    ? String.format("%d days", days)
                    : hours > 0
                    ? String.format("%d hours", hours)
                    : "< hour";

            addChild(new Label(until));
        }

        addChild(new Label(lot.seller));

        final Widget prices = new Widget();
        prices.setLayout(ListLayout.VERTICAL);
        addChild(prices);

        prices.addChild(new Label(String.valueOf(lot.bid)));
        if (lot.hasBuyoutPrice())
            prices.addChild(new Label(String.valueOf(lot.buyoutPrice)));

        final Widget payloadSlots = new Widget();
        payloadSlots.setLayout(ListLayout.HORIZONTAL);
        addChild(payloadSlots);
        for (ItemStack stack : lot.payload)
            payloadSlots.addChild(new ItemStackSlot(stack));
    }

//    public boolean isSelected() {
//        return data.selectedLot == lot;
//    }

//    public void setClickCallback(Consumer<Lot> clickCallback) {
//        this.clickCallback = clickCallback;
//    }

    @Override
    protected void renderSelf() {
        final int color = isSelected() ? 0xdd3377 : 0xffffff;
        RenderShapes.frame(getGeometry(), 1, color);
    }

    public abstract boolean isSelected();

    @Override
    public abstract void onClick(int xMouse, int yMouse, int button);
}
