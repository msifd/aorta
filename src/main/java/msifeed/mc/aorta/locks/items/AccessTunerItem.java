package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.locks.LockTileEntity;
import msifeed.mc.aorta.locks.LockType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

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
    protected void makeBreakSound(LockTileEntity lock) {
        lock.getWorldObj().playSoundEffect(lock.xCoord, lock.yCoord, lock.zCoord, "random.fizz", 0.3f, 3);
    }

    @Override
    protected void doPick(LockTileEntity lock) {
        lock.setSecret(LockItem.DEFAULT_DIGITAL_SECRET);
    }

    @Override
    protected void successMessage(LockTileEntity lock, EntityPlayer player) {
        player.addChatMessage(new ChatComponentTranslation("aorta.lock.hacked"));
        lock.getWorldObj().playSoundEffect(lock.xCoord, lock.yCoord, lock.zCoord, "random.orb", 0.3f, 99999f);
    }
}
