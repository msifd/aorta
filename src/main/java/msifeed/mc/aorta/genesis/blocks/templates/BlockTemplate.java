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
    public boolean pillar = false;

    public BlockTemplate(Material material, String id) {
        super(material);
        setBlockName(id);
    }

    public int getRenderType() {
        if (pillar)
            return 31;
        return 0;
    }

    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        if (pillar)
            return BlockTextureLayout.getPillarMeta(side, meta);
        return meta;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        if (rotatable) {
            final int placedOrt = BlockPistonBase.determineOrientation(world, x, y, z, entity);
            final int layoutMeta = BlockTextureLayout.getRotatableMeta(placedOrt);
            world.setBlockMetadataWithNotify(x, y, z, layoutMeta, 0);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (textureLayout == null)
            return super.getIcon(side, meta);
        if (rotatable)
            return textureLayout.getRotatableIcon(side, meta);
        if (pillar)
            return textureLayout.getPillarIcon(side, meta);
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
