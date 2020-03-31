package msifeed.mc.extensions.mining;

import net.minecraft.nbt.NBTTagCompound;

public class MiningInfo {
    public transient long lastSync = 0;

    public long lastUpdate = 0;
    public long lastMining = 0;
    public double stamina = 1;

    public NBTTagCompound toNBT() {
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setLong(Tags.lastUpdate, lastUpdate);
        nbt.setLong(Tags.lastMining, lastMining);
        nbt.setDouble(Tags.stamina, stamina);
        return nbt;
    }

    public void fromNBT(NBTTagCompound nbt) {
        lastUpdate = nbt.getLong(Tags.lastUpdate);
        lastMining = nbt.getLong(Tags.lastMining);
        stamina = nbt.getDouble(Tags.stamina);
    }

    private static class Tags {
        static final String lastUpdate = "lastUpdate";
        static final String lastMining = "lastMining";
        static final String stamina = "stamina";
    }
}
