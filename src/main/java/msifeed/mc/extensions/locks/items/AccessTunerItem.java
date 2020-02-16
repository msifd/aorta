package msifeed.mc.extensions.locks.items;

import msifeed.mc.Bootstrap;
import msifeed.mc.extensions.locks.LockObject;
import msifeed.mc.extensions.locks.LockType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;

public class AccessTunerItem extends LockpickItem {
    public static final String ID = "lock_access_tuner";

    public AccessTunerItem() {
        setUnlocalizedName(ID);
        setTextureName(Bootstrap.MODID + ":" + ID);
    }

    @Override
    protected boolean canPick(LockObject lock) {
        return lock.getLockType() == LockType.DIGITAL;
    }

    @Override
    protected void makeBreakSound(LockObject lock) {
        final TileEntity te = lock.getTileEntity();
        te.getWorldObj().playSoundEffect(te.xCoord, te.yCoord, te.zCoord, "random.fizz", 0.3f, 3);
    }

    @Override
    protected void doPick(LockObject lock) {
        lock.setSecret(LockItem.DEFAULT_DIGITAL_SECRET);
    }

    @Override
    protected void successMessage(LockObject lock, EntityPlayer player) {
        final TileEntity te = lock.getTileEntity();
        player.addChatMessage(new ChatComponentTranslation("more.lock.hacked"));
        te.getWorldObj().playSoundEffect(te.xCoord, te.yCoord, te.zCoord, "random.orb", 0.3f, 99999f);
    }
}
