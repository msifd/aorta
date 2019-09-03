package msifeed.mc.aorta.books;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.genesis.AortaCreativeTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRemoteBookEditor extends Item {
    public ItemRemoteBookEditor() {
        setUnlocalizedName("remote_book_editor");
        setTextureName("minecraft:paper");
        setCreativeTab(AortaCreativeTab.TOOLS);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        Aorta.GUI_HANDLER.toggleBookEditor(player);
        return itemStack;
    }
}
