package msifeed.mc.aorta.economy.auction;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class Lot {
    public int id;
    public Status status = Status.OPEN;
    public String seller = "";
    public String buyer = "";
    public LocalDateTime expiration;
    public int bid;
    public int buyoutPrice;
    public int cutFee = 5; // in percents
    public ArrayList<ItemStack> payload = new ArrayList<>();

    public Lot() {
    }

    public Lot(NBTTagCompound tag) {
        fromNBT(tag);
    }

    public boolean hasPlayerSeller() {
        return !seller.startsWith("[FRQ]");
    }

    public boolean hasBuyer() {
        return !buyer.isEmpty();
    }

    public boolean hasPlayerBuyer() {
        return hasBuyer() && !buyer.startsWith("[FRQ]");
    }

    public boolean hasExpiration() {
        return expiration.getYear() > 0;
    }

    public boolean hasBuyoutPrice() {
        return buyoutPrice > 0;
    }

    public int calcDepositFee() {
        return payload.stream().mapToInt(Prices::getMSV).sum();
    }

    public int calcCutFee() {
        return bid > 0 ? Math.max(bid * cutFee / 100, 1) : 0;
    }

    public int calcCancellationFee() {
        return calcDepositFee() + calcCutFee();
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("id", id);
        tag.setByte("status", (byte) status.ordinal());
        tag.setString("seller", seller);
        tag.setString("buyer", buyer);
        tag.setLong("exp", expiration.toEpochSecond(ZoneOffset.UTC));
        tag.setInteger("bid", bid);
        tag.setInteger("bout", buyoutPrice);
        tag.setInteger("fee", cutFee);

        final NBTTagList load = new NBTTagList();
        for (ItemStack s : payload) {
            final NBTTagCompound t = new NBTTagCompound();
            s.writeToNBT(t);
            load.appendTag(t);
        }
        tag.setTag("load", load);

        return tag;
    }

    public void fromNBT(NBTTagCompound tag) {
        id = tag.getInteger("id");
        status = Status.values()[tag.getByte("status")];
        seller = tag.getString("seller");
        buyer = tag.getString("buyer");
        expiration = LocalDateTime.ofEpochSecond(tag.getLong("exp"), 0, ZoneOffset.UTC);
        bid = tag.getInteger("bid");
        buyoutPrice = tag.getInteger("bout");
        cutFee = tag.getInteger("fee");

        final NBTTagList load = tag.getTagList("load", 10); // 10 - NBTTagCompound
        for (int i = 0; i < load.tagCount(); i++)
            payload.add(ItemStack.loadItemStackFromNBT(load.getCompoundTagAt(i)));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (ItemStack is : payload) {
            sb.append(is.getDisplayName());
            sb.append(' ');
        }
        sb.append(seller);

        return sb.toString();
    }

    public enum Status {
        OPEN, CLOSED, SOLD
    }
}
