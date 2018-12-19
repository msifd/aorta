package msifeed.mc.aorta.genesis.blocks.templates;

import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.genesis.AortaCreativeTab;
import msifeed.mc.aorta.genesis.blocks.BlockGenesisUnit;
import msifeed.mc.aorta.genesis.blocks.BlockTraitCommons;
import msifeed.mc.aorta.genesis.blocks.SpecialBlockRegisterer;
import msifeed.mc.aorta.locks.DigitalLockAction;
import msifeed.mc.aorta.locks.LockTileEntity;
import msifeed.mc.aorta.locks.LockType;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class DoorTemplate extends BlockDoor implements ITileEntityProvider, BlockTraitCommons.Getter, SpecialBlockRegisterer {
    private BlockTraitCommons traits;
    private final DoorItem item;

    public DoorTemplate(BlockGenesisUnit unit, Material material) {
        super(material);
        traits = new BlockTraitCommons(unit);
        item = new DoorItem(unit, this);

        disableStats();
        setHardness(3);
        setStepSound(material == Material.iron ? soundTypeMetal : soundTypeWood);
        setBlockName(unit.id);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        final LockTileEntity lock = LockTileEntity.find(world, x, y, z);
        if (lock == null)
            return false; // unreachable

        if (player.isSneaking() && lock.getLockType() == LockType.DIGITAL) {
            if (lock.isLocked()) {
                Aorta.GUI_HANDLER.toggleDigitalLock(lock, DigitalLockAction.UNLOCK);
                return true;
            } else {
                Aorta.GUI_HANDLER.toggleDigitalLock(lock, DigitalLockAction.LOCK);
                return true;
            }
        } else if (lock.isLocked()) {
            System.out.println("my locked lock: " + lock.getLockType());
            return true;
        }

        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }
    @Override
    public net.minecraft.item.Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        return item;
    }

    @Override
    public net.minecraft.item.Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return item;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new LockTileEntity();
    }

    @Override
    public BlockTraitCommons getCommons() {
        return traits;
    }

    @Override
    public void register(String id) {
        item.setCreativeTab(AortaCreativeTab.BLOCKS);
        GameRegistry.registerBlock(this, id);
        GameRegistry.registerItem(item, id + "_item");
    }

    public static class DoorItem extends ItemDoor {
        private final DoorTemplate block;

        DoorItem(BlockGenesisUnit unit, DoorTemplate block) {
            super(Material.wood);
            this.block = block;

            setUnlocalizedName(unit.id);
            setTextureName(unit.textureString + "_item");
        }

        public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
            if (side != 1) {
                return false;
            } else {
                ++y;
                if (player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y + 1, z, side, stack)) {
                    if (!block.canPlaceBlockAt(world, x, y, z)) {
                        return false;
                    } else {
                        int i1 = MathHelper.floor_double((double) ((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                        placeDoorBlock(world, x, y, z, i1, block);
                        --stack.stackSize;
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
    }
}
