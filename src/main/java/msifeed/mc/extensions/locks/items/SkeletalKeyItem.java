package msifeed.mc.extensions.locks.items;

import msifeed.mc.Bootstrap;
import msifeed.mc.extensions.locks.LockObject;
import msifeed.mc.extensions.locks.Locks;
import msifeed.mc.more.More;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SkeletalKeyItem extends Item {
    public static final String ID = "lock_skeletal_key";

    public SkeletalKeyItem() {
        setCreativeTab(Locks.LOCKS);
        setUnlocalizedName(ID);
        setTextureName(Bootstrap.MODID + ":" + ID);
    }

    @Override
    public boolean hasEffect(ItemStack par1ItemStack, int pass) {
        return true;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        final LockObject lock = LockObject.find(world, x, y, z);
        if (lock == null || !lock.hasLock())
            return false;

        More.GUI_HANDLER.toggleSkeletalKey(lock);
        return true;
    }
}
