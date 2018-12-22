package msifeed.mc.aorta.locks.items;

import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.genesis.AortaCreativeTab;
import msifeed.mc.aorta.locks.LockTileEntity;
import msifeed.mc.aorta.locks.LockType;
import msifeed.mc.aorta.locks.Locks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class LockItem extends Item {
    public static final String ID_BASE = "lock_";
    static final String DEFAULT_DIGITAL_SECRET = "0000";
    private LockType type;

    public LockItem(LockType type) {
        this.type = type;

        setCreativeTab(AortaCreativeTab.ITEMS);
        setUnlocalizedName(getItemId(type));
        setTextureName("aorta:" + getItemId(type));
    }

    public LockType getLockType() {
        return type;
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

        if (lock.hasLock() && lock.isLocked()) {
            player.addChatMessage(new ChatComponentText("you need to unlock existing lock first"));
            return true;
        }

        player.addChatMessage(new ChatComponentText("install " + type.toString() + " lock"));
        lock.setLockType(type);

        itemStack.stackSize -= 1;

        if (itemStack.hasTagCompound() && !itemStack.getTagCompound().getString("key").isEmpty()) {
            // Used lock has no bonus key
            lock.fromItemStack(itemStack);
        } else {
            final String secret = type == LockType.DIGITAL ? DEFAULT_DIGITAL_SECRET : String.valueOf(world.rand.nextInt());
            lock.setSecret(secret);
            if (type != LockType.DIGITAL)
                player.inventory.addItemStackToInventory(makeKeyItem(secret));
        }

        return true;
    }

    private ItemStack makeKeyItem(String secret) {
        final ItemStack stack = GameRegistry.findItemStack(Aorta.MODID, KeyItem.ID, 1);
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setString("secret", secret);
        stack.setTagCompound(compound);
        return stack;
    }

    public static String getItemId(LockType type) {
        return ID_BASE + type.toString().toLowerCase();
    }
}
