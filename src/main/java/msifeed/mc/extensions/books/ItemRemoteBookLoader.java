package msifeed.mc.extensions.books;

import msifeed.mc.Bootstrap;
import msifeed.mc.genesis.GenesisCreativeTab;
import msifeed.mc.more.More;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRemoteBookLoader extends Item {
    public ItemRemoteBookLoader() {
        setUnlocalizedName("remote_book_loader");
        setTextureName(Bootstrap.MODID + ":remote_book_loader");
        setCreativeTab(GenesisCreativeTab.TOOLS);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        More.GUI_HANDLER.toggleBookLoader(player);
        return itemStack;
    }
}
