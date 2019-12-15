package msifeed.mc.aorta.economy.auction;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.aorta.economy.Economy;
import msifeed.mc.aorta.economy.bank.Currency;
import msifeed.mc.aorta.economy.bank.CurrencyUtils;
import msifeed.mc.aorta.sys.rpc.Rpc;
import msifeed.mc.aorta.sys.rpc.RpcMethod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

public enum AuctionRpc {
    INSTANCE;

    private static final String getLots = "aorta:econ.auc.lots";
    private static final String getLotsResponse = "aorta:econ.auc.lots.re";
    private static final String publishLot = "aorta:econ.auc.publish";
    private static final String bidLot = "aorta:econ.auc.bid";
    private static final String buyoutLot = "aorta:econ.auc.buyout";
    private static final String redeemLot = "aorta:econ.auc.redeem";
    private static final String cancelLot = "aorta:econ.auc.cancel";

    private static int nextLotListRequestId = 0;
    private static HashMap<Integer, Consumer<ArrayList<Lot>>> lotListConsumers = new HashMap<>();

    public static void getOpenLots(String query, Consumer<ArrayList<Lot>> consumer) {
        sendGetLotsRequest(LotsList.OPEN, query, consumer);
    }

    public static void getMyLots(Consumer<ArrayList<Lot>> consumer) {
        sendGetLotsRequest(LotsList.MY, "", consumer);
    }

    private static void sendGetLotsRequest(LotsList type, String query, Consumer<ArrayList<Lot>> consumer) {
        lotListConsumers.put(nextLotListRequestId, consumer);
        Rpc.sendToServer(getLots, nextLotListRequestId, type, query);
        nextLotListRequestId++;
    }

    @RpcMethod(getLots)
    public void onGetLots(MessageContext ctx, int id, LotsList type, String query) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final AuctionData auc = Economy.getAuctionData();
        final Collection<Lot> lotStream;
        if (type == LotsList.OPEN)
            lotStream = auc.getOpenLots();
        else if (type == LotsList.CLOSED)
            lotStream = auc.getClosedLots();
        else if (type == LotsList.MY)
            lotStream = auc.getPlayerLots(player.getCommandSenderName());
        else
            return;

        final NBTTagList lotsNbt = new NBTTagList();
        for (Lot lot : lotStream)
            lotsNbt.appendTag(lot.toNBT());

        try {
            final NBTTagCompound result = new NBTTagCompound();
            result.setTag("l", lotsNbt);
            Rpc.sendTo(player, getLotsResponse, id, CompressedStreamTools.compress(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RpcMethod(getLotsResponse)
    public void onGetLotsResponse(MessageContext ctx, int id, byte[] selectionBytes) {
        try {
            final NBTTagCompound selectionNbt = CompressedStreamTools.func_152457_a(selectionBytes, new NBTSizeTracker(2097152L));
            final NBTTagList slotsNbt = selectionNbt.getTagList("l", 10);
            final ArrayList<Lot> lots = new ArrayList<>();
            for (int i = 0; i < slotsNbt.tagCount(); i++)
                lots.add(new Lot(slotsNbt.getCompoundTagAt(i)));

            final Consumer<ArrayList<Lot>> consumer = lotListConsumers.remove(id);
            if (consumer != null) consumer.accept(lots);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void publishLot(Lot lot) {
        try {
            Rpc.sendToServer(publishLot, (Serializable) CompressedStreamTools.compress(lot.toNBT()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RpcMethod(publishLot)
    public void onPublishLot(MessageContext ctx, byte[] slotBytes) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        try {
            final NBTTagCompound slotNbt = CompressedStreamTools.func_152457_a(slotBytes, new NBTSizeTracker(2097152L));
            final Lot lot = new Lot(slotNbt);
            for (ItemStack is : lot.payload)
                if (Currency.isCurrency(is))
                    throw new AuctionException("can't sell currency");

            Economy.getAuction().publishLot(player, lot);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuctionException e) {
            err(player, e.getMessage());
        }
    }


    public static void bidLot(int lotId, int bid) {
        Rpc.sendToServer(bidLot, lotId, bid);
    }

    @RpcMethod(bidLot)
    public void onBidLot(MessageContext ctx, int lotId, int bid) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final AuctionData auc = Economy.getAuctionData();

        final Lot lot = auc.getOpenLot(lotId);
        if (lot == null) {
            err(player, "no such lot " + lotId);
            return;
        }
        if (lot.status != Lot.Status.OPEN) {
            err(player, "lot is not open");
            return;
        }
        if (lot.bid >= bid) {
            err(player, "bid too small " + bid);
            return;
        }
        final int budget = CurrencyUtils.countPlayerBudget(player);
        if (budget < bid) {
            err(player, "not enough budget " + budget);
            return;
        }

        if (!lot.buyer.isEmpty()) {
            Economy.getBank().updateDeposit(lot.buyer, lot.bid);
        }

        if (!CurrencyUtils.removeCurrencyFromPlayer(player, bid)) {
            return;
        }

        auc.bidLot(lotId, player.getCommandSenderName(), bid);
    }

    public static void buyoutLot(int lotId) {
        Rpc.sendToServer(buyoutLot, lotId);
    }

    @RpcMethod(buyoutLot)
    public void onBuyoutLot(MessageContext ctx, int lotId) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final AuctionData auc = Economy.getAuctionData();

        final Lot lot = auc.getOpenLot(lotId);
        if (lot == null) {
            err(player, "no such lot " + lotId);
            return;
        }
        if (lot.status != Lot.Status.OPEN) {
            err(player, "lot is not open");
            return;
        }
        if (!lot.hasBuyoutPrice()) {
            err(player, "lot has no buyout price");
            return;
        }

        final int budget = CurrencyUtils.countPlayerBudget(player);
        if (budget < lot.buyoutPrice) {
            err(player, "not enough budget " + budget);
            return;
        }

        if (!CurrencyUtils.removeCurrencyFromPlayer(player, lot.buyoutPrice)) {
            return;
        }

        auc.buyoutLot(lotId, player.getCommandSenderName());
    }

    public static void redeemLot(int lotId) {
        Rpc.sendToServer(redeemLot, lotId);
    }

    @RpcMethod(redeemLot)
    public void onRedeemLot(MessageContext ctx, int lotId) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final AuctionData auc = Economy.getAuctionData();

        final Lot lot = auc.getClosedLot(lotId);
        if (lot == null) {
            err(player, "no such lot " + lotId);
            return;
        }
        // TODO: check player permission to redeem
        if (lot.status == Lot.Status.OPEN) {
            err(player, "lot is open");
            return;
        }

        if (lot.hasBuyer()) {
            final int cutFee = lot.calcCutFee();
            final int budget = CurrencyUtils.countPlayerBudget(player);
            if (budget < cutFee) {
                err(player, "not enough budget " + budget);
                return;
            }

            if (!CurrencyUtils.removeCurrencyFromPlayer(player, cutFee)) {
                return;
            }
        }

        for (ItemStack is : lot.payload)
            player.inventory.addItemStackToInventory(is);
        player.inventory.markDirty();

        auc.removeClosedLot(lotId);
    }

    public static void cancelLot(int lotId) {
        Rpc.sendToServer(cancelLot, lotId);
    }

    @RpcMethod(cancelLot)
    public void onCancelLot(MessageContext ctx, int lotId) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final AuctionData auc = Economy.getAuctionData();

        final Lot lot = auc.getOpenLot(lotId);
        if (lot == null) {
            err(player, "no such lot " + lotId);
            return;
        }

        if (lot.status != Lot.Status.OPEN) {
            err(player, "lot is not open");
            return;
        }

        final int cancelFee = lot.calcCancellationFee();
        final int budget = CurrencyUtils.countPlayerBudget(player);
        if (budget < cancelFee) {
            err(player, "not enough budget " + budget);
            return;
        }

        if (!CurrencyUtils.removeCurrencyFromPlayer(player, cancelFee)) {
            return;
        }

        auc.closeLot(lotId);
    }

    private static void err(EntityPlayerMP player, String msg) {
        player.addChatMessage(new ChatComponentText(msg));
    }

    private enum LotsList {
        OPEN, CLOSED, MY
    }
}
