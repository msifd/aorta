package msifeed.mc.aorta.genesis.blocks.templates;

import msifeed.mc.aorta.genesis.GenesisUnit;
import msifeed.mc.aorta.genesis.blocks.BlockTraitCommons;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

public class ChestTemplate extends BlockChest implements BlockTraitCommons.Getter {
    private BlockTraitCommons traits;

    public ChestTemplate(GenesisUnit unit) {
        super(0);
        traits = new BlockTraitCommons(unit);
        setBlockName(unit.id);
        setHardness(2.5F);
        setStepSound(soundTypeWood);
    }

    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityChest();
    }

    @Override
    public BlockTraitCommons getCommons() {
        return traits;
    }
}
