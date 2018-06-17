package msifeed.mc.aorta.genesis.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSlab;
import net.minecraft.world.World;

public class SlabTemplate extends BlockSlab {
    private final String parentName;
    Item item;

    public SlabTemplate(Block parent, boolean isDouble) {
        super(isDouble, parent.getMaterial());
        this.parentName = parent.getUnlocalizedName().substring(5); // Remove `tile.`
    }

    @Override
    public String func_150002_b(int p_150002_1_) {
        return getUnlocalizedName();
    }

    @Override
    public net.minecraft.item.Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        return item;
    }

    public String getName() {
        return parentName + (field_150004_a ? "_doubleslab" : "_slab");
    }

    public static class SlabItem extends ItemSlab {
        public SlabItem(Block block, SlabTemplate halfSlab, SlabTemplate doubleSlab) {
            super(block, halfSlab, doubleSlab, false);

            if (block instanceof SlabTemplate)
                ((SlabTemplate) block).item = this;

            setUnlocalizedName(halfSlab.getName() + "_item");
        }
    }
}
