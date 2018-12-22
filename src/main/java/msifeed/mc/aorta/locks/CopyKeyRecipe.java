package msifeed.mc.aorta.locks;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class CopyKeyRecipe implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting crafting, World world) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting crafting) {
        return null;
    }

    @Override
    public int getRecipeSize() {
        return 0;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
}
