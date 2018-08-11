package msifeed.mc.aorta.genesis.blocks.templates;

import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.aorta.genesis.GenesisUnit;
import msifeed.mc.aorta.genesis.blocks.BlockTraitCommons;
import msifeed.mc.aorta.genesis.blocks.SpecialBlockRegisterer;
import msifeed.mc.aorta.genesis.blocks.client.GenesisChestRenderer;
import msifeed.mc.aorta.things.AortaCreativeTab;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

public class ChestTemplate extends BlockChest implements SpecialBlockRegisterer, BlockTraitCommons.Getter {
    private BlockTraitCommons traits;

    public ChestTemplate(GenesisUnit unit) {
        super(0);
        traits = new BlockTraitCommons(unit);
        setBlockName(unit.id);
        setHardness(2.5F);
        setStepSound(soundTypeWood);
    }

    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new ChestEntity();
    }

    @Override
    public int getRenderType() {
        return GenesisChestRenderer.RENDER_ID;
    }

    @Override
    public String getTextureName() {
        return super.getTextureName();
    }

    @Override
    public BlockTraitCommons getCommons() {
        return traits;
    }

    @Override
    public void register(String id) {
        setCreativeTab(AortaCreativeTab.BLOCKS);
        GameRegistry.registerBlock(this, id);
        GameRegistry.registerTileEntity(ChestTemplate.ChestEntity.class, id);
    }

    public static class ChestEntity extends TileEntityChest {

    }
}
