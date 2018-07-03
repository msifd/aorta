package msifeed.mc.aorta.genesis.blocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import java.util.Arrays;
import java.util.List;

public class BlockTextureLayout {
    private static final int[] ROTATION_MATRIX = new int[]{
            2, 3, 0, 1, 4, 5,
            3, 2, 1, 0, 4, 5,
            0, 1, 2, 3, 4, 5,
            0, 1, 3, 2, 5, 4,
            0, 1, 5, 4, 2, 3,
            0, 1, 4, 5, 3, 2
    };

    private List<String> rawTextures;
    private IIcon[] textures;
    private int[] textureLayout;

    public BlockTextureLayout(List<String> rawTextures, int[] layout) {
        this.rawTextures = rawTextures;
        this.textureLayout = Arrays.copyOf(layout, 6);
    }

    public IIcon getIcon(int side) {
        return textures[textureLayout[side]];
    }

    public IIcon getRotatableIcon(int side, int meta) {
        int ort = (meta & 7) - 1; // Minus default mode - zero
        if (ort >= 0) {
            side = ROTATION_MATRIX[ort * 6 + side];
        }
        return textures[textureLayout[side]];
    }

    public void registerBlockIcons(IIconRegister register) {
        textures = rawTextures.stream()
                .map(register::registerIcon)
                .toArray(IIcon[]::new);
    }

    public static int getRotatableOrientation(int ort) {
        // Zero is default side alignment used in inventory and etc. so add 1. Subtracted in getRotatableIcon.
        return ort + 1;
    }
}
