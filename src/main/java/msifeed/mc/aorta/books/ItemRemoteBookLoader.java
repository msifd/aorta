package msifeed.mc.aorta.books;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.genesis.AortaCreativeTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRemoteBookLoader extends Item {
    public ItemRemoteBookLoader() {
        setUnlocalizedName("remote_book_loader");
        setTextureName("aorta:remote_book_loader");
        setCreativeTab(AortaCreativeTab.TOOLS);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        Aorta.GUI_HANDLER.toggleBookLoader(player);
        return itemStack;
    }
}
