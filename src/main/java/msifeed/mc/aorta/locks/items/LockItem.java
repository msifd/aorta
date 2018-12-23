package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.genesis.AortaCreativeTab;
import msifeed.mc.aorta.locks.LockTileEntity;
import msifeed.mc.aorta.locks.LockType;
import msifeed.mc.aorta.locks.Locks;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

public class LockItem extends Item {
    static final String DEFAULT_DIGITAL_SECRET = "0000";
    private static final String ID_BASE = "lock_";
    private LockType type;

    public LockItem(LockType type) {
        this.type = type;

        setCreativeTab(AortaCreativeTab.LOCKS);
        setUnlocalizedName(getItemId(type));
        setTextureName("aorta:" + getItemId(type));
    }

    public LockType getLockType() {
        return type;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean advanced) {
        if (isBlank(itemStack))
            lines.add(I18n.format("aorta.lock.blank_lock"));
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        final String secret = type == LockType.DIGITAL ? DEFAULT_DIGITAL_SECRET : String.valueOf(world.rand.nextInt());
        final int key = Locks.makeKeyHash(secret);
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("key", key);
        itemStack.setTagCompound(compound);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        final LockTileEntity lock = LockTileEntity.find(world, x, y, z);
        if (lock == null)
            return false;
        install(itemStack, lock, player, world);
        return true;
    }

    private void install(ItemStack itemStack, LockTileEntity lock, EntityPlayer player, World world) {
        if (lock.hasLock() && lock.isLocked())
            return;

        final String messageId = lock.hasLock() ? "aorta.lock.replaced" : "aorta.lock.installed";
        itemStack.stackSize -= 1;

        if (lock.hasLock()) {
            player.inventory.addItemStackToInventory(lock.toItemStack());
            player.inventory.markDirty();
        }

        if (isBlank(itemStack)) {
            final String secret = type == LockType.DIGITAL ? DEFAULT_DIGITAL_SECRET : String.valueOf(world.rand.nextInt());
            lock.setSecret(secret);
            lock.setLockType(type);
            if (type != LockType.DIGITAL) {
                player.inventory.addItemStackToInventory(KeyItem.makeKeyItem(secret));
                player.inventory.markDirty();
            }
        } else {
            // Used lock has no bonus key
            lock.fromItemStack(itemStack);
        }

        if (!world.isRemote)
            player.addChatMessage(new ChatComponentTranslation(messageId));
    }

    private boolean isBlank(ItemStack itemStack) {
        return !itemStack.hasTagCompound() || itemStack.getTagCompound().getString("key").isEmpty();
    }

    public static String getItemId(LockType type) {
        return ID_BASE + type.toString().toLowerCase();
    }
}
