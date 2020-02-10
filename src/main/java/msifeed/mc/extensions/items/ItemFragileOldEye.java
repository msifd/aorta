package msifeed.mc.extensions.items;

import msifeed.mc.Bootstrap;
import msifeed.mc.genesis.GenesisCreativeTab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFragileOldEye extends Item {
    public static String ITEM_NAME = "fragile_old_eye";

    public ItemFragileOldEye() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName(Bootstrap.MODID + ":" + ITEM_NAME);
        setCreativeTab(GenesisCreativeTab.ITEMS);
        setMaxStackSize(16);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return 0;
    }
}
