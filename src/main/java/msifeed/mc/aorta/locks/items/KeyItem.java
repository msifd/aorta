package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.genesis.AortaCreativeTab;
import msifeed.mc.aorta.locks.LockTileEntity;
import msifeed.mc.aorta.locks.LockType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class KeyItem extends Item {
    public static final String ID = "lock_key";

    public KeyItem() {
        setCreativeTab(AortaCreativeTab.ITEMS);
        setUnlocalizedName(ID);
        setTextureName("aorta:" + ID);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        final LockTileEntity lock = LockTileEntity.find(world, x, y, z);
        if (lock == null)
            return false;

        if (lock.getLockType() == LockType.BUILD_IN || lock.getLockType() == LockType.PADLOCK) {
            lock.toggleLocked();
            player.addChatMessage(new ChatComponentText(lock.isLocked() ? "locked" : "unlocked"));
        }

        return true;
    }
}
