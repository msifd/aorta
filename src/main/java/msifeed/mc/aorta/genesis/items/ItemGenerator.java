package msifeed.mc.aorta.genesis.items;

import com.google.gson.JsonObject;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.genesis.Generator;
import msifeed.mc.aorta.genesis.GenesisTrait;
import msifeed.mc.aorta.genesis.items.templates.FoodTemplate;
import msifeed.mc.aorta.genesis.items.templates.ItemTemplate;
import msifeed.mc.aorta.things.AortaCreativeTab;
import net.minecraft.item.Item;

import java.util.HashSet;

import static msifeed.mc.aorta.genesis.GenesisTrait.*;

public class ItemGenerator implements Generator {
    @Override
    public void generate(JsonObject json, HashSet<GenesisTrait> traits) {
        final ItemGenesisUnit unit = new ItemGenesisUnit(json, traits);

        final Item item = getItemTemplate(unit);
        fillCommons(unit, item);
        GameRegistry.registerItem(item, unit.id);
    }

    private Item getItemTemplate(ItemGenesisUnit unit) {
        if (unit.hasTrait(consumable))
            return new FoodTemplate(unit);
        return new ItemTemplate(unit);
    }

    private void fillCommons(ItemGenesisUnit unit, Item item) {
        item.setCreativeTab(AortaCreativeTab.ITEMS);

        if (unit.hasTrait(not_stackable))
            item.setMaxStackSize(1);
        if (unit.hasTrait(hold_like_tool))
            item.setFull3D();

        if (FMLCommonHandler.instance().getSide().isClient()) {
            fillTexture(unit, item);
        }
    }

    @SideOnly(Side.CLIENT)
    private void fillTexture(ItemGenesisUnit unit, Item item) {
        if (unit.texture != null) {
            item.setTextureName(unit.texture);
        }
    }
}
