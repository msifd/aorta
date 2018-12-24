package msifeed.mc.aorta.tools;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.genesis.AortaCreativeTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDesignerTool extends Item {
    public static String ID = "tool_designer";

    public ItemDesignerTool() {
        setUnlocalizedName(ID);
        setTextureName("aorta:" + ID);
        setCreativeTab(AortaCreativeTab.TOOLS);
        setMaxStackSize(1);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return 0;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote)
            Aorta.GUI_HANDLER.toggleDesignerScreen();
        return itemStack;
    }
}
