package msifeed.mc.aorta.genesis.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import java.util.Arrays;
import java.util.List;

public class GenesisBlock extends Block {
    protected List<String> rawTextures;
    protected IIcon[] textures;
    protected int[] textureLayout;

    public GenesisBlock(Material material) {
        super(material);
    }

    @SideOnly(Side.CLIENT)
    public void setTextures(List<String> rawTextures, int[] layout) {
        this.rawTextures = rawTextures;
        if (layout != null)
            this.textureLayout = Arrays.copyOf(layout, 6);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (textureLayout == null) {
            return super.getIcon(side, meta);
        } else {
            return textures[textureLayout[side]];
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        if (textureLayout == null) {
            super.registerBlockIcons(register);
        } else {
            textures = rawTextures.stream()
                    .map(register::registerIcon)
                    .toArray(IIcon[]::new);
        }
    }
}
