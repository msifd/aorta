package msifeed.mc.aorta.books;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.genesis.AortaCreativeTab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemRemoteBook extends Item {
    private IIcon icons[] = new IIcon[RemoteBook.Style.values().length];

    public ItemRemoteBook() {
        setUnlocalizedName("remote_book");
        setCreativeTab(AortaCreativeTab.TOOLS);
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (itemStack.hasTagCompound())
            Aorta.GUI_HANDLER.toggleBookViewer(player);
        else
            Aorta.GUI_HANDLER.toggleBookEditor(player);
        return itemStack;
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        if (!itemStack.hasTagCompound())
            return super.getItemStackDisplayName(itemStack);
        return itemStack.getTagCompound().getString("title");
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean advanced) {
        if (!advanced || !itemStack.hasTagCompound())
            return;
        lines.add("Index: " + itemStack.getTagCompound().getString("value"));
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        if (meta == 0)
            return itemIcon;
        return icons[meta - 1];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon("aorta:remote_book");

        final RemoteBook.Style[] styles = RemoteBook.Style.values();
        for (int i = 0; i < styles.length; i++) {
            final String tx = "aorta:remote_book_" + styles[i].toString().toLowerCase();
            icons[i] = iconRegister.registerIcon(tx);
        }
    }
}
