package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.locks.LockTileEntity;
import msifeed.mc.aorta.locks.LockType;

public class AccessTunerItem extends LockpickItem {
    public static final String ID = "lock_access_tuner";

    public AccessTunerItem() {
        setUnlocalizedName(ID);
        setTextureName("aorta:" + ID);
    }

    @Override
    protected boolean canPick(LockTileEntity lock) {
        return lock.getLockType() == LockType.DIGITAL;
    }

    @Override
    protected void unlock(LockTileEntity lock) {
        lock.setSecret(LockItem.DEFAULT_DIGITAL_SECRET);
    }
}
