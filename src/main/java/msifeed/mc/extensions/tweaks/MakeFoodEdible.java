package msifeed.mc.extensions.tweaks;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;

public class MakeFoodEdible {
    public static void apply() {
        for (Item i : GameData.getItemRegistry().typeSafeIterable()) {
            if (i instanceof ItemFood)
                ((ItemFood) i).setAlwaysEdible();
        }
    }
}
