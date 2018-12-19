package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.locks.LockTileEntity;

public class SkeletalKeyItem extends LockpickItem {
    public static final String ID = "lock_skeletal_key";

    public SkeletalKeyItem() {
        setUnlocalizedName(ID);
        setTextureName("aorta:" + ID);
    }

    @Override
    protected boolean canPick(LockTileEntity lock) {
        return true;
    }
}
