package msifeed.mc.aorta.tools;

import msifeed.mc.Bootstrap;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.genesis.GenesisCreativeTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDesignerTool extends Item {
    public static String ID = "tool_designer";

    public ItemDesignerTool() {
        setUnlocalizedName(ID);
        setTextureName(Bootstrap.MODID + ":" + ID);
        setCreativeTab(GenesisCreativeTab.TOOLS);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote)
            Aorta.GUI_HANDLER.toggleDesignerScreen();
        return itemStack;
    }
}
