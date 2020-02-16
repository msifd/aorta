package msifeed.mc.more.content;

import cpw.mods.fml.common.registry.GameRegistry;

public class Items {
    public static void init() {
        GameRegistry.registerItem(new ItemOldEye(), ItemOldEye.ITEM_NAME);
        GameRegistry.registerItem(new ItemFragileOldEye(), ItemFragileOldEye.ITEM_NAME);
        GameRegistry.registerItem(new ItemConvictionIndict(), ItemConvictionIndict.ITEM_NAME);
        GameRegistry.registerItem(new ItemForgivnessStone(), ItemForgivnessStone.ITEM_NAME);
        GameRegistry.registerItem(new ItemConvictsList(), ItemConvictsList.ITEM_NAME);
        GameRegistry.registerItem(new ItemGifterOffering(), ItemGifterOffering.ITEM_NAME);
        GameRegistry.registerItem(new ItemSoulGrab(), ItemSoulGrab.ITEM_NAME);
    }
}
