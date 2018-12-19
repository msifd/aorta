package msifeed.mc.aorta.locks;

import msifeed.mc.aorta.genesis.blocks.templates.DoorTemplate;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class LockTileEntity extends TileEntity {
    static final String ID = "aorta.lock";

    private LockType type = LockType.NONE;
    private int key;
    private boolean locked;

    public boolean hasLock() {
        return type != LockType.NONE;
    }

    public LockType getLockType() {
        return type;
    }

    public void setLockType(LockType type) {
        this.type = type;
        markDirty();
    }

    public void setSecret(String secret) {
        this.key = Locks.makeKeyHash(secret);
    }

    public boolean isLocked() {
        return locked;
    }

    public void toggleLocked() {
        setLocked(!locked);
    }

    public void setLocked(boolean locked) {
        System.out.println("set lock to " + locked);
        this.locked = locked;
        markDirty();
        soundToggleLock();
    }

    public boolean canUnlockWith(String secret) {
        return this.key == Locks.makeKeyHash(secret);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        System.out.println("LockTileEntity is invalidated!");
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.type = LockType.values()[compound.getByte("type")];
        this.key = compound.getInteger("key");
        this.locked = compound.getBoolean("locked");
        System.out.println("loaded");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setByte("type", (byte) type.ordinal());
        compound.setInteger("key", this.key);
        compound.setBoolean("locked", this.locked);
    }

    @Override
    public Packet getDescriptionPacket() {
        final NBTTagCompound compound = new NBTTagCompound();
        this.writeToNBT(compound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());
        System.out.println("received update. now locked: " + locked);
    }

    private void soundToggleLock() {
        final float pitch = worldObj.rand.nextFloat() * 0.1f + 0.5f;
        worldObj.playSoundEffect(xCoord, yCoord, zCoord, "random.click", 0.3f, pitch);
    }

    public static LockTileEntity find(World world, int x, int y, int z) {
        final Block middleBlock = world.getBlock(x, y, z);

        if (!(middleBlock instanceof DoorTemplate))
            return null;

        final Block upperBlock = world.getBlock(x, y + 1, z);
        final int groundBlockY = upperBlock instanceof DoorTemplate ? y : y - 1;

        final TileEntity te = world.getTileEntity(x, groundBlockY, z);
        if (te instanceof LockTileEntity)
            return (LockTileEntity) te;
        else
            return null;
    }
}
