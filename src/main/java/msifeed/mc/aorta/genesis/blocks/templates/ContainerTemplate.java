package msifeed.mc.aorta.genesis.blocks.templates;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.genesis.blocks.BlockTraitCommons;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Random;

public class ContainerTemplate extends BlockContainer implements BlockTraitCommons.Getter {
    static {
        GameRegistry.registerTileEntity(TileEntityContainer.class, "AortaContainer");
    }

    private BlockTraitCommons traits = new BlockTraitCommons();

    private final int rows;

    public ContainerTemplate(Material material, String id, int rows) {
        super(material);
        this.rows = rows;
        setBlockName(id);
    }

    @Override
    public BlockTraitCommons getCommons() {
        return traits;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityContainer(rows * 9, getUnlocalizedName());
    }

    @Override
    public int getRenderType() {
        return traits.getRenderType();
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        return traits.onBlockPlaced(side, meta);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        traits.onBlockPlacedBy(world, x, y, z, entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (traits.textureLayout == null)
            return super.getIcon(side, meta);
        return traits.getIcon(side, meta);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        if (traits.textureLayout != null)
            traits.textureLayout.registerBlockIcons(register);
        else
            super.registerBlockIcons(register);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float lx, float ly, float lz) {
        if (world.isRemote) return true;

        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityContainer) {
            player.displayGUIChest((TileEntityContainer) te);
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
        if (world.isRemote)
            return;

        final TileEntity teRaw = world.getTileEntity(x, y, z);
        if (!(teRaw instanceof IInventory))
            return;

        final IInventory inv = (IInventory) teRaw;
        final ArrayList<ItemStack> drops = new ArrayList<>();
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null)
                drops.add(stack.copy());
        }

        final Random rand = new Random();
        for (ItemStack drop : drops) {
            final EntityItem item = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, drop);
            if (FMLCommonHandler.instance().getSide().isClient())
                item.setVelocity((rand.nextDouble() - 0.5) * 0.25, rand.nextDouble() * 0.5 * 0.25, (rand.nextDouble() - 0.5) * 0.25);
            world.spawnEntityInWorld(item);
        }
    }

    public static class TileEntityContainer extends TileEntity implements IInventory {
        private ItemStack[] items = new ItemStack[54];
        private int size = 54;
        private String name;

        public TileEntityContainer() {
        }

        public TileEntityContainer(int size, String name) {
            this.size = size;
            this.name = name;
        }

        public int getSizeInventory() {
            return this.size;
        }

        public ItemStack getStackInSlot(int slot) {
            return items[slot];
        }

        public ItemStack decrStackSize(int slot, int amount) {
            if (items[slot] != null) {
                ItemStack itemstack;

                if (items[slot].stackSize == amount) {
                    itemstack = items[slot];
                    items[slot] = null;
                    markDirty();
                    return itemstack;
                } else {
                    itemstack = items[slot].splitStack(amount);
                    if (items[slot].stackSize == 0) items[slot] = null;
                    markDirty();
                    return itemstack;
                }
            } else {
                return null;
            }
        }

        public ItemStack getStackInSlotOnClosing(int slot) {
            if (items[slot] != null) {
                ItemStack itemstack = items[slot];
                items[slot] = null;
                return itemstack;
            } else {
                return null;
            }
        }

        public void setInventorySlotContents(int slot, ItemStack stack) {
            items[slot] = stack;
            if (stack != null && stack.stackSize > getInventoryStackLimit()) {
                stack.stackSize = getInventoryStackLimit();
            }
            markDirty();
        }

        public String getInventoryName() {
            return name + ".name";
        }

        public boolean hasCustomInventoryName() {
            return false;
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {
            super.readFromNBT(nbt);

            size = nbt.getByte("Size");
            name = nbt.getString("Name");

            NBTTagList list = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
            items = new ItemStack[getSizeInventory()];
            for (int i = 0; i < list.tagCount(); ++i) {
                NBTTagCompound comp = list.getCompoundTagAt(i);
                int j = comp.getByte("Slot");
                if (j < items.length) {
                    items[j] = ItemStack.loadItemStackFromNBT(comp);
                }
            }
        }

        @Override
        public void writeToNBT(NBTTagCompound nbt) {
            super.writeToNBT(nbt);

            nbt.setByte("Size", (byte) size);
            nbt.setString("Name", name);

            NBTTagList list = new NBTTagList();
            for (int i = 0; i < getSizeInventory(); ++i) {
                if (items[i] != null) {
                    NBTTagCompound comp = new NBTTagCompound();
                    comp.setByte("Slot", (byte) i);
                    items[i].writeToNBT(comp);
                    list.appendTag(comp);
                }
            }
            nbt.setTag("Items", list);
        }

        public int getInventoryStackLimit() {
            return 64;
        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer player) {
            return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this
                    && player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 20.0D;
        }

        public void openInventory() {
        }

        public void closeInventory() {
        }

        public boolean isItemValidForSlot(int slot, ItemStack stack) {
            return true;
        }
    }
}
