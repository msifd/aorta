package msifeed.mc.extensions.items;

import msifeed.mc.Bootstrap;
import msifeed.mc.genesis.GenesisCreativeTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemConvictsList extends Item {
    public static String ITEM_NAME = "convicts_list";

    public ItemConvictsList() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName(Bootstrap.MODID + ":" + ITEM_NAME);
        setCreativeTab(GenesisCreativeTab.ITEMS);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        return itemStack;
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return 0;
    }
}
