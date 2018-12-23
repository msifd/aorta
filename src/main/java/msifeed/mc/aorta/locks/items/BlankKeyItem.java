package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.genesis.AortaCreativeTab;
import net.minecraft.item.Item;

public class BlankKeyItem extends Item {
    public static final String ID = "lock_blank_key";

    public BlankKeyItem() {
        setCreativeTab(AortaCreativeTab.LOCKS);
        setUnlocalizedName(ID);
        setTextureName("aorta:" + ID);
    }
}
