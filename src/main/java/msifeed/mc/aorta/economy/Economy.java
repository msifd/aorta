package msifeed.mc.aorta.economy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.economy.auction.AuctionData;
import msifeed.mc.aorta.economy.auction.AuctionManager;
import msifeed.mc.aorta.economy.bank.BankData;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

public enum Economy {
    INSTANCE;

    private BankData bankData = null;
    private AuctionManager auctionManager = new AuctionManager();

    public static void init() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
        INSTANCE.auctionManager.init();
    }

    public static BankData getBank() {
        return INSTANCE.bankData;
    }

    public static AuctionManager getAuction() {
        return INSTANCE.auctionManager;
    }

    public static AuctionData getAuctionData() {
        return INSTANCE.auctionManager.getData();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (bankData == null)
            loadBankData(event.world);
        auctionManager.load(event.world);
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world.provider.dimensionId == 0) {
            bankData = null;
            auctionManager.unload();
        }
    }

    private void loadBankData(World world) {
        final WorldSavedData wsd = world.loadItemData(BankData.class, BankData.DATA_NAME);
        if (wsd == null) {
            bankData = new BankData();
        } else if (wsd instanceof BankData)
            bankData = (BankData) wsd;
        world.setItemData(BankData.DATA_NAME, bankData);
    }

    private void loadAuctionData(World world) {
    }
}
