package msifeed.mc.aorta.genesis.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockTemplate extends Block {
    protected BlockTextureLayout textureLayout = null;

    public BlockTemplate(Material material) {
        super(material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return textureLayout != null
                ? textureLayout.getIcon(side, meta)
                : super.getIcon(side, meta);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        super.registerBlockIcons(register);
        if (textureLayout != null)
            textureLayout.registerBlockIcons(register);
    }
}
