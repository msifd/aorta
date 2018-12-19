package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.genesis.AortaCreativeTab;
import msifeed.mc.aorta.locks.LockTileEntity;
import msifeed.mc.aorta.locks.LockType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class LockItem extends Item {
    public static final String ID_BASE = "lock_";
    private static final String DEFAULT_DIGITAL_SECRET = "0000";
    private LockType type;

    public LockItem(LockType type) {
        this.type = type;

        setCreativeTab(AortaCreativeTab.ITEMS);
        setUnlocalizedName(ID_BASE + type.toString().toLowerCase());
        setTextureName("aorta:" + ID_BASE + type.toString().toLowerCase());
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        final LockTileEntity lock = LockTileEntity.find(world, x, y, z);
        if (lock == null)
            return false;

        if (lock.hasLock() && lock.isLocked()) {
            player.addChatMessage(new ChatComponentText("you need to unlock existing lock first"));
        }

        player.addChatMessage(new ChatComponentText("install " + type.toString() + " lock"));
        lock.setLockType(type);
        if (type == LockType.DIGITAL)
            lock.setSecret(DEFAULT_DIGITAL_SECRET);

        return true;
    }
}
