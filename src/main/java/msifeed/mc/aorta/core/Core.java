package msifeed.mc.aorta.core;

import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.aorta.core.character.CharacterProperty;
import msifeed.mc.aorta.core.things.ItemBattleStick;

public class Core {
    public void init() {
//        MinecraftForge.EVENT_BUS.register(CharacterManager.INSTANCE);
        CharacterProperty.register();

        GameRegistry.registerItem(new ItemBattleStick(), ItemBattleStick.ITEM_NAME);
    }
}
