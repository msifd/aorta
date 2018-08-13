package msifeed.mc.aorta.genesis.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemTemplate extends Item {
    private final ItemGenesisUnit unit;

    public ItemTemplate(ItemGenesisUnit unit) {
        this.unit = unit;
        setUnlocalizedName(unit.id);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        return unit.rarity.color.toString() + super.getItemStackDisplayName(itemStack);
    }
}
