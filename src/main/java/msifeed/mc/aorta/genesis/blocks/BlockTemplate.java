package msifeed.mc.aorta.genesis.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
    private static final int[] ROTATION_MATRIX = new int[]{
            2, 3, 0, 1, 4, 5,
            3, 2, 1, 0, 4, 5,
            0, 1, 2, 3, 4, 5,
            0, 1, 3, 2, 5, 4,
            0, 1, 5, 4, 2, 3,
            0, 1, 4, 5, 3, 2
    };

    protected BlockTextureLayout textureLayout = null;
    protected boolean rotatable = false;

    public BlockTemplate(Material material) {
        super(material);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        // Zero is default side alignment used in inventory and etc. Subtracted in getIcon.
        final int ort = 1 + BlockPistonBase.determineOrientation(world, x, y, z, entity);
        world.setBlockMetadataWithNotify(x, y, z, ort, 0);
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

        int ort = (meta & 7) - 1; // Minus default mode - zero
        if (rotatable && ort >= 0) {
            side = ROTATION_MATRIX[ort * 6 + side];
        }

        return textureLayout.getIcon(side, meta);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        super.registerBlockIcons(register);
        if (textureLayout != null)
            textureLayout.registerBlockIcons(register);
    }
}
