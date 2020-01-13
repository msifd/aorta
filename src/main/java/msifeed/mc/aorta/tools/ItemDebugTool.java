package msifeed.mc.aorta.tools;

import msifeed.mc.Bootstrap;
import msifeed.mc.genesis.GenesisCreativeTab;
import net.minecraft.item.Item;

public class ItemDebugTool extends Item {
    public static String ITEM_NAME = "tool_debug";

    public ItemDebugTool() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName(Bootstrap.MODID + ":" + ITEM_NAME);
        setCreativeTab(GenesisCreativeTab.TOOLS);
        setMaxStackSize(1);
    }
}
