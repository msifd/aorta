package msifeed.mc.aorta.economy.bank;

import msifeed.mc.aorta.Aorta;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

import java.util.HashMap;
import java.util.Map;

public class BankData extends WorldSavedData {
    public static final String DATA_NAME = Aorta.MODID + ".econ.bank";

    private int pool = 0;
    private HashMap<String, Integer> playerDeposits = new HashMap<>();

    public BankData() {
        this(DATA_NAME);
    }

    public BankData(String dataNameFromStorage) {
        super(DATA_NAME);
        if (!DATA_NAME.equals(dataNameFromStorage))
            throw new RuntimeException("Invalid Bank storage data name: " + dataNameFromStorage);
    }

    public void updatePool(int diff) {
        pool += diff;
        markDirty();
    }

    public void updateDeposit(String name, int diff) {
        final Integer dep = playerDeposits.get(name);
        if (diff > 0) {
            if (dep == null)
                playerDeposits.put(name, diff);
            else
                playerDeposits.put(name, dep + diff);
            markDirty();
        } else if (diff < 0 && dep != null) {
            if (dep == diff)
                playerDeposits.remove(name);
            else
                playerDeposits.put(name, dep - diff);
            markDirty();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound root) {
        pool = root.getInteger("pool");

        playerDeposits.clear();
        final NBTTagList deposits = root.getTagList("deposits", 10);
        for (int i = 0; i < deposits.tagCount(); i++) {
            final NBTTagCompound p = deposits.getCompoundTagAt(i);
            final String name = p.getString("n");
            final int amount = p.getInteger("a");
            if (!name.isEmpty() && amount > 0)
                playerDeposits.put(name, amount);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound root) {
        root.setInteger("pool", pool);

        final NBTTagList deposits = new NBTTagList();
        for (Map.Entry<String, Integer> dep : playerDeposits.entrySet()) {
            final NBTTagCompound p = new NBTTagCompound();
            p.setString("n", dep.getKey());
            p.setInteger("a", dep.getValue());
            deposits.appendTag(p);
        }
        root.setTag("deposits", deposits);
    }
}
