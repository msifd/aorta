package msifeed.mc.aorta.genesis.items;

import msifeed.mc.aorta.genesis.rename.RenameProvider;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemCommons {
    public static void addInformation(ItemGenesisUnit unit, ItemStack itemStack, List<String> lines) {
        if (RenameProvider.hasDescription(itemStack))
            RenameProvider.addDescriptionToList(itemStack, lines);
        else if (unit.desc != null)
            Collections.addAll(lines, unit.desc);

        final LinkedHashMap<String, String> values = new LinkedHashMap<>(unit.values);
        values.putAll(RenameProvider.getOverriddenValues(itemStack));
        for (Map.Entry<String, String> entry : values.entrySet())
            lines.add("\u00A7r" + entry.getKey() + "\u00A7r: " + entry.getValue());
    }
}
