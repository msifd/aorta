package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.core.rolls.FeatureRoll;
import msifeed.mc.aorta.locks.LockObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class AdvancedLockpickItem extends LockpickItem {
    public static final String ID = "lock_advanced_lockpick";

    public AdvancedLockpickItem() {
        setUnlocalizedName(ID);
        setTextureName("aorta:" + SkeletalKeyItem.ID);
    }

    @Override
    protected boolean rollPick(LockObject lock, ItemStack pick, EntityPlayer player) {
        consumePick(lock, pick, player, null);
        return true;
    }

    @Override
    protected void consumePick(LockObject lock, ItemStack pick, EntityPlayer player, FeatureRoll roll) {
        if (lock.getTileEntity().getWorldObj().isRemote)
            return;
        pick.stackSize--;
        makeBreakSound(lock);
    }
}
