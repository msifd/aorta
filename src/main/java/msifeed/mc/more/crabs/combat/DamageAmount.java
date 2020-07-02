package msifeed.mc.more.crabs.combat;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public final class DamageAmount {
    public boolean piecing;
    public float amount;

    public DamageAmount(DamageSource source, float amount) {
        this.piecing = source.isUnblockable();
        this.amount = amount;
    }

    public DamageAmount(NBTTagCompound c) {
        fromNBT(c);
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound c = new NBTTagCompound();
        c.setBoolean(Tags.piecing, piecing);
        c.setFloat(Tags.amount, amount);
        return c;
    }

    public void fromNBT(NBTTagCompound c) {
        piecing = c.getBoolean(Tags.piecing);
        amount = c.getFloat(Tags.amount);
    }

    private static final class Tags {
        static final String piecing = "piecing";
        static final String amount = "amount";
    }
}
