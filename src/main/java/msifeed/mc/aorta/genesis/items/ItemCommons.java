package msifeed.mc.aorta.genesis.items;

import msifeed.mc.aorta.genesis.GenesisTrait;
import msifeed.mc.aorta.genesis.rename.RenameProvider;
import msifeed.mc.aorta.sys.utils.L10n;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemCommons {
    public static void addInformation(ItemGenesisUnit unit, ItemStack itemStack, List<String> lines) {
        final List<String> customDesc = RenameProvider.getDescription(itemStack);
        if (!customDesc.isEmpty())
            lines.addAll(customDesc);
        else if (unit.desc != null)
            Collections.addAll(lines, unit.desc);

        final LinkedHashMap<String, String> values = new LinkedHashMap<>(unit.values);
        values.putAll(RenameProvider.getOverriddenValues(itemStack));
        for (Map.Entry<String, String> entry : values.entrySet())
            if (!entry.getValue().isEmpty())
                lines.add("\u00A7r" + entry.getKey() + "\u00A7r: " + entry.getValue());

        if (unit.maxUsages > 0 && !unit.hasTrait(GenesisTrait.hidden_uses))
            lines.add("\u00A7r" + L10n.fmt("aorta.uses_left", itemStack.getItemDamage()));
    }
}
