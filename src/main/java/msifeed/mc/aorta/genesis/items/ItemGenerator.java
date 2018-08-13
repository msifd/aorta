package msifeed.mc.aorta.genesis.items;

import com.google.gson.JsonObject;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.genesis.Generator;
import msifeed.mc.aorta.genesis.GenesisTrait;
import msifeed.mc.aorta.things.AortaCreativeTab;

import java.util.HashSet;

import static msifeed.mc.aorta.genesis.GenesisTrait.hold_like_tool;
import static msifeed.mc.aorta.genesis.GenesisTrait.not_stackable;

public class ItemGenerator implements Generator {
    @Override
    public void generate(JsonObject json, HashSet<GenesisTrait> traits) {
        final ItemGenesisUnit unit = new ItemGenesisUnit(json, traits);

        final ItemTemplate item = new ItemTemplate(unit);
        fillCommons(unit, item);
        GameRegistry.registerItem(item, unit.id);
    }

    private void fillCommons(ItemGenesisUnit unit, ItemTemplate item) {
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
    private void fillTexture(ItemGenesisUnit unit, ItemTemplate item) {
        if (unit.texture != null) {
            item.setTextureName(unit.texture);
        }
    }
}
