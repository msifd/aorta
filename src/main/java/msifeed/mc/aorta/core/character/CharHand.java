package msifeed.mc.aorta.core.character;

import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;

public class CharHand {
    private static HashMap<Integer, List<ItemStack>> playersHands = new HashMap<>();

    public static ItemStack getHandItem(int entityId, int index) {
        final List<ItemStack> list = playersHands.get(entityId);
        if (list == null || index >= list.size())
            return null;
        return list.get(index);
    }

    public static void setHand(int entityId, List<ItemStack> items) {
        playersHands.put(entityId, items);
    }
}
