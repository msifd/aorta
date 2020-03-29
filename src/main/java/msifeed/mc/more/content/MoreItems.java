package msifeed.mc.more.content;

import cpw.mods.fml.common.registry.GameRegistry;

public class MoreItems {
    public static void init() {
        GameRegistry.registerItem(ItemOldEye.makeNormal(), ItemOldEye.NORMAL_ITEM_NAME);
        GameRegistry.registerItem(ItemOldEye.makeFragile(), ItemOldEye.FRAGILE_ITEM_NAME);
        GameRegistry.registerItem(new ItemConvictionIndict(), ItemConvictionIndict.ITEM_NAME);
        GameRegistry.registerItem(new ItemForgivnessStone(), ItemForgivnessStone.ITEM_NAME);
        GameRegistry.registerItem(new ItemConvictsList(), ItemConvictsList.ITEM_NAME);
        GameRegistry.registerItem(new ItemGifterOffering(), ItemGifterOffering.ITEM_NAME);
        GameRegistry.registerItem(new ItemSoulGrab(), ItemSoulGrab.ITEM_NAME);
    }
}
