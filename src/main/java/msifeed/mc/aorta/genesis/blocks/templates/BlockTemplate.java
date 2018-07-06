package msifeed.mc.aorta.genesis.blocks.templates;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.genesis.blocks.BlockTextureLayout;
import msifeed.mc.aorta.genesis.blocks.BlockTraitCommons;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockTemplate extends Block implements BlockTraitCommons.Getter {
    private BlockTraitCommons traits = new BlockTraitCommons();

    public BlockTemplate(Material material, String id) {
        super(material);
        setBlockName(id);
    }

    @Override
    public BlockTraitCommons getCommons() {
        return traits;
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
}
