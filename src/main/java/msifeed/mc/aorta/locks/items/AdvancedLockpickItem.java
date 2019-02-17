package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.core.rules.FeatureRoll;
import msifeed.mc.aorta.locks.LockTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;

public class AdvancedLockpickItem extends LockpickItem {
    public static final String ID = "lock_advanced_lockpick";

    public AdvancedLockpickItem() {
        setUnlocalizedName(ID);
        setTextureName("aorta:" + SkeletalKeyItem.ID);
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
}
