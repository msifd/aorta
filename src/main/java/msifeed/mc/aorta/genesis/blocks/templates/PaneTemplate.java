package msifeed.mc.aorta.genesis.blocks.templates;

import msifeed.mc.aorta.genesis.blocks.BlockGenesisUnit;
import msifeed.mc.aorta.genesis.blocks.BlockTraitCommons;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;

public class PaneTemplate extends BlockPane implements BlockTraitCommons.Getter {
    private BlockTraitCommons traits;

    public PaneTemplate(BlockGenesisUnit unit, Material material) {
        super(unit.textureString, unit.textureString + "_top", material, false);
        traits = new BlockTraitCommons(unit);
    }

    @Override
    public BlockTraitCommons getCommons() {
        return traits;
    }
}
