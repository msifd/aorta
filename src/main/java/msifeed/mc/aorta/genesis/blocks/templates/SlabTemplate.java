package msifeed.mc.aorta.genesis.blocks.templates;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSlab;
import net.minecraft.world.World;

public class SlabTemplate extends BlockSlab {
    private final String selfId;
    private Item item;

    public SlabTemplate(Block parent, boolean isDouble, String id) {
        super(isDouble, parent.getMaterial());
        this.selfId = id;
        setBlockName(id);

        if (isDouble)
            item = Item.getItemFromBlock(parent); // Refer to parent block on middle click
    }

    @Override
    public String func_150002_b(int meta) {
        return getUnlocalizedName();
    }

    @Override
    public net.minecraft.item.Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        return item;
    }

    public static class SlabItem extends ItemSlab {
        public SlabItem(Block block, SlabTemplate halfSlab, SlabTemplate doubleSlab) {
            super(block, halfSlab, doubleSlab, false);

            if (block instanceof SlabTemplate)
                ((SlabTemplate) block).item = this;

            setUnlocalizedName(halfSlab.selfId + "_item");
        }
    }
}
