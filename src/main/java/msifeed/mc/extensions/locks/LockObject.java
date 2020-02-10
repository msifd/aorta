package msifeed.mc.extensions.locks;

import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.Bootstrap;
import msifeed.mc.more.More;
import msifeed.mc.extensions.locks.items.LockItem;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class LockObject {
    private final transient TileEntity tile;
    private LockType type = LockType.NONE;
    private int key;
    private boolean locked;
    private int difficulty = More.DEFINES.get().locks.defaultDifficulty;

    public LockObject(TileEntity tile) {
        this.tile = tile;
    }

    public TileEntity getTileEntity() {
        return tile;
    }

    public void updateTileEntity() {
        tile.markDirty();
        tile.getWorldObj().markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
    }

    public boolean hasLock() {
        return type != LockType.NONE;
    }

    public LockType getLockType() {
        return type;
    }

    public void setLockType(LockType type) {
        this.type = type;
        this.difficulty = type == LockType.NONE ? 0 : More.DEFINES.get().locks.defaultDifficulty;
        this.locked = false;
        updateTileEntity();
    }

    public void setSecret(String secret) {
        this.key = Locks.makeKeyHash(secret);
        updateTileEntity();
    }

    public boolean isLocked() {
        return locked;
    }

    public void toggleLocked() {
        setLocked(!locked);
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
        updateTileEntity();
        makeToggleSound();
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        updateTileEntity();
    }

    public boolean canUnlockWith(String secret) {
        return this.key == Locks.makeKeyHash(secret);
    }

    void makeToggleSound() {
        tile.getWorldObj().playSoundEffect(tile.xCoord, tile.yCoord, tile.zCoord, "random.click", 0.3f, 3);
    }

    EntityItem makeEntityItem() {
        final float f = 0.7F;
        final World w = tile.getWorldObj();
        final double d0 = w.rand.nextFloat() * f + (1.0F - f) * 0.5D;
        final double d1 = w.rand.nextFloat() * f + (1.0F - f) * 0.5D;
        final double d2 = w.rand.nextFloat() * f + (1.0F - f) * 0.5D;
        final EntityItem entityitem = new EntityItem(w, tile.xCoord + d0, tile.yCoord + d1, tile.zCoord + d2, toItemStack());
        entityitem.delayBeforeCanPickup = 10;
        return entityitem;
    }

    public ItemStack toItemStack() {
        final ItemStack stack = GameRegistry.findItemStack(Bootstrap.MODID, LockItem.getItemId(type), 1);
        if (stack == null)
            return null;

        final NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("key", key);
        compound.setByte("diff", (byte) difficulty);
        stack.setTagCompound(compound);
        return stack;
    }

    public void fromItemStack(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof LockItem) || !itemStack.hasTagCompound())
            return;

        final LockItem item = (LockItem) itemStack.getItem();
        final NBTTagCompound compound = itemStack.getTagCompound();
        this.type = item.getLockType();
        this.key = compound.getInteger("key");
        this.difficulty = compound.getByte("diff");
        updateTileEntity();
    }

    public void readFromNBT(NBTTagCompound compound) {
        final NBTTagCompound lc = compound.getCompoundTag("aorta.lock");
        this.type = LockType.values()[lc.getByte("type")];
        this.key = lc.getInteger("key");
        this.difficulty = lc.getByte("diff");
        this.locked = lc.getBoolean("locked");
    }

    public void writeToNBT(NBTTagCompound compound) {
        final NBTTagCompound lc = new NBTTagCompound();
        lc.setByte("type", (byte) type.ordinal());
        lc.setInteger("key", this.key);
        lc.setByte("diff", (byte) this.difficulty);
        lc.setBoolean("locked", this.locked);
        compound.setTag("aorta.lock", lc);
    }

    public void copyFrom(LockObject l) {
        this.type = l.type;
        this.key = l.key;
        this.locked = l.locked;
        this.difficulty = l.difficulty;
        updateTileEntity();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LockObject))
            return false;
        final LockObject o = (LockObject) obj;
        return type == o.type
                && key == o.key
                && locked == o.locked
                && difficulty == o.difficulty;
    }

    public static LockObject find(World world, int x, int y, int z) {
        final Block block = world.getBlock(x, y, z);
        return block instanceof LockableBlock
            ? ((LockableBlock) block).getLock(world, x, y, z)
            : null;
    }
}
