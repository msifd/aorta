package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.locks.LockTileEntity;
import msifeed.mc.aorta.locks.LockType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class KeyItem extends Item {
    public static final String ID = "lock_key";
    public static final String TEX_BASE = "lock_key_";

    public KeyItem() {
        setUnlocalizedName(ID);
        setTextureName("aorta:" + TEX_BASE + "1");
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!itemStack.hasTagCompound())
            return false;

        final LockTileEntity lock = LockTileEntity.find(world, x, y, z);
        if (lock == null || lock.getLockType() == LockType.DIGITAL)
            return false;

        final String secret = itemStack.getTagCompound().getString("secret");
        if (lock.canUnlockWith(secret)) {
            lock.toggleLocked();

            if (!world.isRemote) {
                final ChatComponentText ct = new ChatComponentText(lock.isLocked() ? "*locked*" : "*unlocked*");
                ct.getChatStyle().setColor(EnumChatFormatting.GRAY);
                player.addChatMessage(ct);
            }
        }

        return true;
    }
}
