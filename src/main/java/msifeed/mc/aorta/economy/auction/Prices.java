package msifeed.mc.aorta.economy.auction;

import msifeed.mc.aorta.genesis.items.IItemTemplate;
import msifeed.mc.aorta.genesis.items.ItemRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class Prices {
    private static HashMap<ItemRarity, Integer> rarityPrices = new HashMap<>();

    static {
        rarityPrices.put(ItemRarity.POOR, 0);
        rarityPrices.put(ItemRarity.COMMON, 1);
        rarityPrices.put(ItemRarity.UNCOMMON, 3);
        rarityPrices.put(ItemRarity.RARE, 8);
        rarityPrices.put(ItemRarity.EPIC, 21);
        rarityPrices.put(ItemRarity.LEGENDARY, 55);
    }

    public static int getMSV(ItemStack is) {
        if (is == null) return 0;
        final Item i = is.getItem();
        final ItemRarity r = i instanceof IItemTemplate
                ? ((IItemTemplate) i).getUnit().rarity
                : ItemRarity.COMMON;
        return rarityPrices.get(r) * is.stackSize;
    }
}
