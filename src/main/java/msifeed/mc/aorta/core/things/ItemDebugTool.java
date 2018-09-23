package msifeed.mc.aorta.core.things;

import msifeed.mc.aorta.genesis.AortaCreativeTab;
import net.minecraft.item.Item;

public class ItemDebugTool extends Item {
    public static String ITEM_NAME = "tool_debug";

    public ItemDebugTool() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName("aorta:tool_debug");
        setCreativeTab(AortaCreativeTab.TOOLS);
        setMaxStackSize(1);
    }
}
