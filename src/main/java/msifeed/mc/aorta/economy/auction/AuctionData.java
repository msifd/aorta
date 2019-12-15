package msifeed.mc.aorta.economy.auction;

import msifeed.mc.aorta.Aorta;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class AuctionData extends WorldSavedData {
    public static final String DATA_NAME = Aorta.MODID + ".econ.auc";

    private int nextLotId = 0;
    private HashMap<Integer, Lot> openLots = new HashMap<>();
    private HashMap<Integer, Lot> closedLots = new HashMap<>();
    private HashMap<String, HashSet<Lot>> playerLots = new HashMap<>();

    public AuctionData() {
        this(DATA_NAME);
    }

    public AuctionData(String dataNameFromStorage) {
        super(DATA_NAME);
        if (!DATA_NAME.equals(dataNameFromStorage))
            throw new RuntimeException("Invalid Auction storage data name: " + dataNameFromStorage);
    }

    public Collection<Lot> getOpenLots() {
        return openLots.values();
    }

    public Collection<Lot> getClosedLots() {
        return closedLots.values();
    }

    public Collection<Lot> getPlayerLots(String name) {
        final HashSet<Lot> pl = playerLots.get(name);
        if (pl == null) return Collections.emptyList();
        return pl;
    }

    public Lot getOpenLot(int lotId) {
        return openLots.get(lotId);
    }

    public Lot getClosedLot(int lotId) {
        return closedLots.get(lotId);
    }

    public void refLot(String name, Lot lot) {
        final HashSet<Lot> pl = playerLots.computeIfAbsent(name, s -> new HashSet<>());
        pl.add(lot);
        markDirty();
    }

    public void derefLot(String name, Lot lot) {
        final HashSet<Lot> pl = playerLots.get(name);
        if (pl == null) return;
        pl.remove(lot);
        if (pl.isEmpty())
            playerLots.remove(name);
        markDirty();
    }

    public void addLot(Lot lot) {
        lot.id = nextLotId;
        lot.status = Lot.Status.OPEN;
        openLots.put(nextLotId, lot);
        if (lot.hasPlayerSeller())
            refLot(lot.seller, lot);
        nextLotId++;
        markDirty();
    }

    public void bidLot(int lotId, String buyer, int bid) {
        final Lot lot = openLots.get(lotId);
        if (lot == null) return;

        if (lot.hasPlayerBuyer())
            derefLot(lot.buyer, lot);
        lot.buyer = buyer;
        if (lot.hasPlayerBuyer())
            refLot(lot.buyer, lot);
        lot.bid = bid;
        markDirty();
    }

    public void buyoutLot(int lotId, String buyer) {
        final Lot lot = openLots.get(lotId);
        if (lot == null) return;

        openLots.remove(lotId);
        closedLots.put(lotId, lot);
        if (lot.hasPlayerBuyer())
            derefLot(lot.buyer, lot);
        lot.buyer = buyer;
        if (lot.hasPlayerBuyer())
            refLot(lot.buyer, lot);
        lot.bid = lot.buyoutPrice;
        markDirty();
    }

    public void closeLot(int lotId) {
        final Lot lot = openLots.get(lotId);
        if (lot == null) return;

        openLots.remove(lotId);
        if (lot.hasBuyer()) {
            if (lot.hasPlayerBuyer()) {
                lot.status = Lot.Status.SOLD;
                derefLot(lot.seller, lot);
                refLot(lot.buyer, lot);
                closedLots.put(lotId, lot);
            }
        } else {
            if (lot.hasPlayerSeller()) {
                lot.status = Lot.Status.CLOSED;
                closedLots.put(lotId, lot);
            }
        }
        markDirty();
    }

    public void removeClosedLot(int lotId) {
        final Lot lot = closedLots.remove(lotId);
        if (lot == null) return;

        if (lot.hasPlayerSeller() && !lot.hasBuyer())
            derefLot(lot.seller, lot);
        if (lot.hasPlayerBuyer())
            derefLot(lot.buyer, lot);
        markDirty();
    }

//    public void removeActiveLot(int lotId) {
//        if (!hasOpenLot(lotId)) return;
//        openLots.remove(lotId);
//        markDirty();
//    }

    public void clearLots() {
        openLots.clear();
        closedLots.clear();
        playerLots.clear();
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound root) {
        nextLotId = root.getInteger("nextLotId");

        final NBTTagList activeLotsNbt = root.getTagList("openLots", 10); // 10 - NBTTagCompound
        for (int i = 0; i < activeLotsNbt.tagCount(); i++) {
            final Lot l = new Lot(activeLotsNbt.getCompoundTagAt(i));
            openLots.put(l.id, l);
        }

        final NBTTagList closedLotsNbt = root.getTagList("closedLots", 10); // 10 - NBTTagCompound
        for (int i = 0; i < closedLotsNbt.tagCount(); i++) {
            final Lot l = new Lot(closedLotsNbt.getCompoundTagAt(i));
            closedLots.put(l.id, l);
        }

        for (Lot l : openLots.values()) {
            if (l.hasPlayerSeller()) refLot(l.seller, l);
            if (l.hasPlayerBuyer()) refLot(l.buyer, l);
        }
        for (Lot l : closedLots.values()) {
            if (l.hasPlayerSeller()) refLot(l.seller, l);
            if (l.hasPlayerBuyer()) refLot(l.buyer, l);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound root) {
        root.setInteger("nextLotId", nextLotId);

        final NBTTagList activeLotsNbt = new NBTTagList();
        for (Lot s : openLots.values())
            activeLotsNbt.appendTag(s.toNBT());
        root.setTag("openLots", activeLotsNbt);

        final NBTTagList closedLotsNbt = new NBTTagList();
        for (Lot s : closedLots.values())
            closedLotsNbt.appendTag(s.toNBT());
        root.setTag("closedLots", closedLotsNbt);
    }
}
