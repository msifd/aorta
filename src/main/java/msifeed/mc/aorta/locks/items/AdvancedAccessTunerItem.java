package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.core.rules.FeatureRoll;
import msifeed.mc.aorta.locks.LockTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;

public class AdvancedAccessTunerItem extends AccessTunerItem {
    public static final String ID = "lock_advanced_tuner";

    public AdvancedAccessTunerItem() {
        setUnlocalizedName(ID);
        setTextureName("aorta:" + ID);
    }

    @Override
    protected boolean rollPick(LockTileEntity lock, ItemStack pick, EntityPlayer player) {
        consumePick(lock, pick, player, null);
        return true;
    }

    @Override
    protected void consumePick(LockTileEntity lock, ItemStack pick, EntityPlayer player, FeatureRoll roll) {
        if (lock.getWorldObj().isRemote)
            return;
        pick.stackSize--;
        makeBreakSound(lock);
    }

    @Override
    protected void doPick(LockTileEntity lock) {
        lock.setSecret(LockItem.DEFAULT_DIGITAL_SECRET);
        lock.setLocked(!lock.isLocked());
    }
}
