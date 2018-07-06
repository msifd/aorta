package msifeed.mc.aorta.genesis.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockTraitCommons {
    public BlockTextureLayout textureLayout = null;
    public boolean rotatable = false;
    public boolean pillar = false;

    public int getRenderType() {
        if (pillar)
            return 31;
        return 0;
    }

    public int onBlockPlaced(int side, int meta) {
        if (pillar)
            return BlockTextureLayout.getPillarMeta(side, meta);
        return meta;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity) {
        if (rotatable) {
            final int placedOrt = BlockPistonBase.determineOrientation(world, x, y, z, entity);
            final int layoutMeta = BlockTextureLayout.getRotatableMeta(placedOrt);
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
    
    public interface Getter {
        BlockTraitCommons getCommons();
    }
}
