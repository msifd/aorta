package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.genesis.AortaCreativeTab;
import msifeed.mc.aorta.locks.LockTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SkeletalKeyItem extends Item {
    public static final String ID = "lock_skeletal_key";

    public SkeletalKeyItem() {
        setCreativeTab(AortaCreativeTab.LOCKS);
        setUnlocalizedName(ID);
        setTextureName("aorta:" + ID);
    }

    @Override
    public boolean hasEffect(ItemStack par1ItemStack, int pass) {
        return true;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        final LockTileEntity lock = LockTileEntity.find(world, x, y, z);
        if (lock == null || !lock.hasLock())
            return false;
        Aorta.GUI_HANDLER.toggleSkeletalKey(lock);
        return true;
    }
}
