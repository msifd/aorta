package msifeed.mc.aorta.core.things;

import msifeed.mc.aorta.things.AortaCreativeTab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemDebugTool extends Item {
    public static String ITEM_NAME = "debug_tool";

    public ItemDebugTool() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName("netherbrick");
        setCreativeTab(AortaCreativeTab.ITEMS);
        setMaxStackSize(1);
    }

    @Override
    public boolean hasEffect(ItemStack par1ItemStack, int pass) {
        return true;
    }
}
