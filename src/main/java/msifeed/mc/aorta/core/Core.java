package msifeed.mc.aorta.core;

import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.aorta.core.character.CharacterProperty;
import msifeed.mc.aorta.core.things.ItemBattleTool;
import msifeed.mc.aorta.core.things.ItemCharTool;
import msifeed.mc.aorta.core.things.ItemDebugTool;

public class Core {
    public void init() {
        CharacterProperty.register();

        GameRegistry.registerItem(new ItemDebugTool(), ItemDebugTool.ITEM_NAME);
        GameRegistry.registerItem(new ItemCharTool(), ItemCharTool.ITEM_NAME);
        GameRegistry.registerItem(new ItemBattleTool(), ItemBattleTool.ITEM_NAME);
    }
}
