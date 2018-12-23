package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.genesis.AortaCreativeTab;
import msifeed.mc.aorta.locks.LockTileEntity;
import msifeed.mc.aorta.locks.LockType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class LockpickItem extends Item {
    public static final String ID = "lock_lockpick";

    public LockpickItem() {
        setCreativeTab(AortaCreativeTab.LOCKS);
        setUnlocalizedName(ID);
        setTextureName("aorta:" + ID);
    }

    protected boolean canPick(LockTileEntity lock) {
        return lock.getLockType() == LockType.BUILD_IN || lock.getLockType() == LockType.PADLOCK;
    }

    protected void unlock(LockTileEntity lock) {
        lock.setLocked(false);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        final LockTileEntity lock = LockTileEntity.find(world, x, y, z);
        if (lock == null)
            return false;

        if (!lock.hasLock() || !lock.isLocked()) {
            return false;
        }

        if (canPick(lock)) {
            if (!world.isRemote)
                player.addChatMessage(new ChatComponentTranslation("aorta.lock.unlocked"));
            unlock(lock);
        }

        return true;
    }
}
