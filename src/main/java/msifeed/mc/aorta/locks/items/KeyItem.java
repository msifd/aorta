package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.genesis.AortaCreativeTab;
import msifeed.mc.aorta.locks.LockTileEntity;
import msifeed.mc.aorta.locks.LockType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class KeyItem extends Item {
    public static final String ID = "lock_key";
    public static final String TEX_BASE = "lock_key_";

    public KeyItem() {
        setCreativeTab(AortaCreativeTab.ITEMS);
        setUnlocalizedName(ID);
        setTextureName("aorta:" + TEX_BASE + "1");
    }

    public boolean isEmpty(ItemStack itemStack) {
        if (!itemStack.hasTagCompound())
            return true;
        final NBTTagCompound compound = itemStack.getTagCompound();
        return compound.getString("secret").isEmpty();
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!itemStack.hasTagCompound())
            return false;

        final LockTileEntity lock = LockTileEntity.find(world, x, y, z);
        if (lock == null || lock.getLockType() == LockType.DIGITAL)
            return false;

        final NBTTagCompound compound = itemStack.getTagCompound();
        final String secret = compound.getString("secret");
        if (lock.canUnlockWith(secret)) {
            lock.toggleLocked();
            player.addChatMessage(new ChatComponentText(lock.isLocked() ? "locked" : "unlocked"));
        }

        return true;
    }

    public static class Empty extends KeyItem {

    }
}
