package msifeed.mc.aorta.genesis.blocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockTextureLayout {
    protected List<String> rawTextures;
    protected IIcon[] textures;
    protected int[] textureLayout;

    public BlockTextureLayout(ArrayList<String> rawTextures, int[] layout) {
        this.rawTextures = rawTextures;
        this.textureLayout = Arrays.copyOf(layout, 6);
    }

    public IIcon getIcon(int side, int meta) {
        return textures[textureLayout[side]];
    }

    public void registerBlockIcons(IIconRegister register) {
        textures = rawTextures.stream()
                .map(register::registerIcon)
                .toArray(IIcon[]::new);
    }
}
