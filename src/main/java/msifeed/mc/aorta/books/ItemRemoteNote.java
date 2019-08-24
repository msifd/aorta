package msifeed.mc.aorta.books;

import msifeed.mc.aorta.Aorta;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRemoteNote extends ItemRemoteBook {
    public ItemRemoteNote() {
        super();
        setUnlocalizedName("remote_note");
        setTextureName("minecraft:paper");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (itemStack.hasTagCompound())
            Aorta.GUI_HANDLER.toggleBookViewer(player);
        else
            Aorta.GUI_HANDLER.toggleNoteEditor(player);
        return itemStack;
    }
}
