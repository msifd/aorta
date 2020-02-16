package msifeed.mc.more.tools;

import cpw.mods.fml.common.registry.GameRegistry;

public class ToolItems {
    public static void init() {
        GameRegistry.registerItem(new ItemDebugTool(), ItemDebugTool.ITEM_NAME);
        GameRegistry.registerItem(new ItemCharTool(), ItemCharTool.ITEM_NAME);
        GameRegistry.registerItem(new ItemStatusTool(), ItemStatusTool.ITEM_NAME);
        GameRegistry.registerItem(new ItemCombatTool(), ItemCombatTool.ITEM_NAME);
        GameRegistry.registerItem(new ItemDesignerTool(), ItemDesignerTool.ID);
    }
}
