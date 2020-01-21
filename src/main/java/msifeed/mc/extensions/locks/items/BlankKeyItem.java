package msifeed.mc.extensions.locks.items;

import msifeed.mc.Bootstrap;
import msifeed.mc.extensions.locks.Locks;
import net.minecraft.item.Item;

public class BlankKeyItem extends Item {
    public static final String ID = "lock_blank_key";

    public BlankKeyItem() {
        setCreativeTab(Locks.LOCKS);
        setUnlocalizedName(ID);
        setTextureName(Bootstrap.MODID + ":" + ID);
    }
}
