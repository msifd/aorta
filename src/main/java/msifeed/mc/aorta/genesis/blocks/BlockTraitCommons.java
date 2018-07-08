package msifeed.mc.aorta.genesis.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTraitCommons {
    private static final int[] ROTATION_MATRIX = new int[]{
            2, 3, 0, 1, 4, 5,
            3, 2, 1, 0, 4, 5,
            0, 1, 2, 3, 4, 5,
            0, 1, 3, 2, 5, 4,
            0, 1, 5, 4, 2, 3,
            0, 1, 4, 5, 3, 2
    };

    public BlockTextureLayout textureLayout = null;
    public boolean rotatable = false;
    public boolean pillar = false;
    public boolean half = false;
    public Size size = Size.MEDIUM;

    public boolean isSolid(int side, int meta) {
        if (half)
            return side == Facing.oppositeSide[getOrt(meta)];
        return true;
    }

    public int getRenderType() {
        if (pillar)
            return 31;
        return 0;
    }

    public int onBlockPlaced(int side, int meta) {
        if (pillar)
            return getPillarMeta(side, meta);
        return meta;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity) {
        if (rotatable) {
            final int placedOrt = BlockPistonBase.determineOrientation(world, x, y, z, entity);
            final int layoutMeta = getRotatableMeta(placedOrt);
            world.setBlockMetadataWithNotify(x, y, z, layoutMeta, 0);
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (rotatable)
            return textureLayout.getRotatableIcon(side, meta);
        if (pillar)
            return textureLayout.getPillarIcon(side, meta);
        return textureLayout.getIcon(side);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z) {
        if (half) {
            final int ort = getOrt(access.getBlockMetadata(x, y, z));
            final float halfSize;
            switch (size) {
                case SMALL:
                    halfSize = 0.25f;
                    break;
                case LARGE:
                    halfSize = 0.75f;
                    break;
                default:
                    halfSize = 0.5f;
                    break;
            }

            final float value = ort % 2 == 0 ? 1 - halfSize : halfSize;
            final float minY = ort == 0 ? value : 0;
            final float minZ = ort == 2 ? value : 0;
            final float minX = ort == 4 ? value : 0;
            final float maxY = ort == 1 ? value : 1;
            final float maxZ = ort == 3 ? value : 1;
            final float maxX = ort == 5 ? value : 1;
            access.getBlock(x, y, z).setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }

    public int getOrt(int meta) {
        if (rotatable)
            return getRotatedOrt(meta);
        if (pillar)
            return getPillarOrt(meta);
        return 1;
    }

    public static int getRotatedOrt(int meta) {
        return (meta & 7) - 1; // Minus default mode - zero
    }

    public static int getPillarOrt(int meta) {
        final int t = meta & 12;
        final int f = meta % 2;
        switch (t) {
            default:
                return f;
            case 8:
                return 2 | f;
            case 4:
                return 4 | f;
        }
    }

    public static int getRotatedSide(int side, int meta) {
        int ort = getRotatedOrt(meta);
        if (ort >= 0) {
            side = ROTATION_MATRIX[ort * 6 + side];
        }
        return side;
    }

    public static int getRotatableMeta(int ort) {
        // Zero is default side alignment used in inventory and etc. so add 1. Subtracted in getRotatableIcon.
        return ort + 1;
    }

    public static int getPillarMeta(int side, int meta) {
        byte b = (byte) (side % 2);
        switch (side) {
            case 2:
            case 3:
                b |= 8;
                break;
            case 4:
            case 5:
                b |= 4;
                break;
        }
        return meta | b;
    }

    public enum Size {
        SMALL, MEDIUM, LARGE
    }
    
    public interface Getter {
        BlockTraitCommons getCommons();
    }
}
