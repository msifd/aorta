package msifeed.mc.aorta.genesis.blocks.templates;

import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.aorta.genesis.GenesisUnit;
import msifeed.mc.aorta.genesis.blocks.BlockTraitCommons;
import msifeed.mc.aorta.genesis.blocks.SpecialBlockRegisterer;
import msifeed.mc.aorta.things.AortaCreativeTab;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class DoorTemplate extends BlockDoor implements BlockTraitCommons.Getter, SpecialBlockRegisterer {
    private BlockTraitCommons traits;
    private final Item item;

    public DoorTemplate(GenesisUnit unit, Material material) {
        super(material);
        traits = new BlockTraitCommons(unit);
        item = new Item(unit.id, this);

        disableStats();
        setHardness(3);
        setStepSound(material == Material.iron ? soundTypeMetal : soundTypeWood);
        setBlockName(unit.id);
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
    public BlockTraitCommons getCommons() {
        return traits;
    }

    @Override
    public void register(String id) {
        item.setCreativeTab(AortaCreativeTab.BLOCKS);
        GameRegistry.registerBlock(this, id);
        GameRegistry.registerItem(item, id + "_item");
    }

    public static class Item extends ItemDoor {
        private final DoorTemplate block;

        Item(String id, DoorTemplate block) {
            super(Material.wood);
            this.block = block;

            setUnlocalizedName(id + "_item");
            setTextureName(id + "_item");
        }

        public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
            if (p_77648_7_ != 1) {
                return false;
            } else {
                ++p_77648_5_;

                if (p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_) && p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_ + 1, p_77648_6_, p_77648_7_, p_77648_1_)) {
                    if (!block.canPlaceBlockAt(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_)) {
                        return false;
                    } else {
                        int i1 = MathHelper.floor_double((double) ((p_77648_2_.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                        placeDoorBlock(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, i1, block);
                        --p_77648_1_.stackSize;
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
    }
}
