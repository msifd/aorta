package msifeed.mc.aorta.genesis.blocks.templates;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.genesis.blocks.BlockTextureLayout;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockTemplate extends Block {
    public BlockTextureLayout textureLayout = null;
    public boolean rotatable = false;

    public BlockTemplate(Material material, String id) {
        super(material);
        setBlockName(id);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        if (rotatable) {
            final int placedOrt = BlockPistonBase.determineOrientation(world, x, y, z, entity);
            final int layoutOrt = BlockTextureLayout.getRotatableOrientation(placedOrt);
            world.setBlockMetadataWithNotify(x, y, z, layoutOrt, 0);
        }
    }

    @Override
    protected ItemStack createStackedBlock(int meta) {
        return new ItemStack(Item.getItemFromBlock(this), 1, meta & 3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (textureLayout == null)
            return super.getIcon(side, meta);
        if (rotatable)
            return textureLayout.getRotatableIcon(side, meta);
        return textureLayout.getIcon(side);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        if (textureLayout != null)
            textureLayout.registerBlockIcons(register);
        else
            super.registerBlockIcons(register);
    }
}
