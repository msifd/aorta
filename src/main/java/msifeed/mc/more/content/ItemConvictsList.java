package msifeed.mc.more.content;

import msifeed.mc.Bootstrap;
import msifeed.mc.genesis.GenesisCreativeTab;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemConvictsList extends Item {
    public static String ITEM_NAME = "convicts_list";

    public ItemConvictsList() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName(Bootstrap.MODID + ":" + ITEM_NAME);
        setCreativeTab(GenesisCreativeTab.ITEMS);
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean extra) {
        lines.add(L10n.tr("item.convicts_list.desc1"));
        lines.add(L10n.tr("item.convicts_list.desc2"));
        lines.add(L10n.tr("item.convicts_list.desc3"));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        return itemStack;
    }
}
