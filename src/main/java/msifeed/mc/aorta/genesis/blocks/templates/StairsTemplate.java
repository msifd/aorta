package msifeed.mc.aorta.genesis.blocks.templates;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

public class StairsTemplate extends BlockStairs {
    public StairsTemplate(Block parent, String id) {
        super(parent, 0);
        setBlockName(id);
    }
}
