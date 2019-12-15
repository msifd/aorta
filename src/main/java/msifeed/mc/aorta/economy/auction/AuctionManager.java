package msifeed.mc.aorta.economy.auction;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.aorta.economy.Economy;
import msifeed.mc.aorta.economy.bank.CurrencyUtils;
import msifeed.mc.aorta.sys.rpc.Rpc;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;

public class AuctionManager {
    private static final int UPDATE_PERIOD = 1000;

    private long lastUpdateOnTick = 0;
    private AuctionData auctionData = null;

    public void init() {
        FMLCommonHandler.instance().bus().register(this);
        GameRegistry.registerBlock(new AuctionTerminalBlock(), AuctionTerminalBlock.BLOCK_ID);
        Rpc.register(AuctionRpc.INSTANCE);
    }

    public void load(World world) {
        if (auctionData != null) return;

        final WorldSavedData wsd = world.loadItemData(AuctionData.class, AuctionData.DATA_NAME);
        if (wsd == null) {
            auctionData = new AuctionData();
        } else if (wsd instanceof AuctionData)
            auctionData = (AuctionData) wsd;
        world.setItemData(AuctionData.DATA_NAME, auctionData);
    }

    public void unload() {
        auctionData = null;
    }

    public AuctionData getData() {
        return auctionData;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || System.currentTimeMillis() - lastUpdateOnTick < UPDATE_PERIOD) return;

        final AuctionData auc = Economy.getAuctionData();
        final LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        for (Lot lot : auc.getOpenLots()) {
//            if (now.isBefore(lot.expiration)) continue;
            auc.closeLot(lot.id);
        }

        lastUpdateOnTick = System.currentTimeMillis();
    }

    public void publishLot(EntityPlayerMP player, Lot lot) {
        final HashSet<Integer> invItems = new HashSet<>(lot.payload.size());
        final ItemStack[] inv = player.inventory.mainInventory;

        for (ItemStack lis : lot.payload) {
            for (int i = 0; i < inv.length; i++) {
                final ItemStack is = inv[i];
                if (ItemStack.areItemStacksEqual(lis, is) && !invItems.contains(i)) {
                    invItems.add(i);
                    break;
                }
            }
        }

        if (invItems.size() != lot.payload.size())
            return;

        final int budget = CurrencyUtils.countPlayerBudget(player);
        final int depositFee = lot.calcDepositFee();
        if (budget < depositFee)
            throw new AuctionException("not enough budget " + budget);

        if (!CurrencyUtils.removeCurrencyFromPlayer(player, depositFee))
            throw new AuctionException("can't take deposit fee without exchange");

        for (Integer i : invItems)
            player.inventory.mainInventory[i] = null;

        Economy.getBank().updatePool(depositFee);
        Economy.getAuctionData().addLot(lot);
    }
}
