package msifeed.mc.genesis.items.templates;

import msifeed.mc.genesis.items.IItemTemplate;
import msifeed.mc.genesis.items.ItemCommons;
import msifeed.mc.genesis.items.ItemGenesisUnit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import java.util.List;

import static msifeed.mc.genesis.GenesisTrait.*;

public class FoodTemplate extends ItemFood implements IItemTemplate {
    private final ItemGenesisUnit unit;

    public FoodTemplate(ItemGenesisUnit unit) {
        super(0, getSaturation(unit), false);
        this.unit = unit;
        setUnlocalizedName(unit.id);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        final String name = unit.title != null
                ? unit.title
                : super.getItemStackDisplayName(itemStack);
        return unit.rarity.color.toString() + name;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean debug) {
        ItemCommons.addInformation(unit, itemStack, lines);
    }

    @Override
    public ItemGenesisUnit getUnit() {
        return unit;
    }

    private static int getSaturation(ItemGenesisUnit unit) {
        if (unit.hasTrait(no_saturation))
            return 0;
        if (unit.hasTrait(large))
            return 10;
        if (unit.hasTrait(small))
            return 3;
        if (unit.hasTrait(tiny))
            return 1;
        return 6;
    }

    private static int getUseDuration(ItemGenesisUnit unit) {
        if (unit.hasTrait(large))
            return 64;
        if (unit.hasTrait(small) || unit.hasTrait(tiny))
            return 16;
        return 32;
    }
}
