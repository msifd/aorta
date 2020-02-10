package msifeed.mc.extensions.books;

import msifeed.mc.more.More;
import msifeed.mc.genesis.GenesisCreativeTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRemoteBookEditor extends Item {
    public ItemRemoteBookEditor() {
        setUnlocalizedName("remote_book_editor");
        setTextureName("minecraft:paper");
        setCreativeTab(GenesisCreativeTab.TOOLS);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        More.GUI_HANDLER.toggleBookEditor(player);
        return itemStack;
    }
}
