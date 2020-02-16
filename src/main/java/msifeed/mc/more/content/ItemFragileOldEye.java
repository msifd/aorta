package msifeed.mc.more.content;

import msifeed.mc.Bootstrap;
import msifeed.mc.genesis.GenesisCreativeTab;
import net.minecraft.item.Item;

public class ItemFragileOldEye extends Item {
    public static String ITEM_NAME = "fragile_old_eye";

    public ItemFragileOldEye() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName(Bootstrap.MODID + ":" + ITEM_NAME);
        setCreativeTab(GenesisCreativeTab.ITEMS);
        setMaxStackSize(16);
    }
}
