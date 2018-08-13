package msifeed.mc.aorta.genesis.items;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import msifeed.mc.aorta.genesis.GenesisTrait;
import msifeed.mc.aorta.genesis.GenesisUnit;

import java.util.HashSet;

import static msifeed.mc.aorta.genesis.GenesisTrait.*;

public class ItemGenesisUnit extends GenesisUnit {
    public String texture = null;
    public final ItemRarity rarity;

    public ItemGenesisUnit(JsonObject json, HashSet<GenesisTrait> traits) {
        super(json, traits);
        rarity = getRarity();

        if (json.has(Props.texture)) {
            final JsonElement tEl = json.get(Props.texture);
            if (tEl.isJsonPrimitive() && tEl.getAsJsonPrimitive().isString()) {
                texture = tEl.getAsJsonPrimitive().getAsString();
            }
        }
    }

    private ItemRarity getRarity() {
        if (hasTrait(legendary))
            return ItemRarity.LEGENDARY;
        else if (hasTrait(epic))
            return ItemRarity.EPIC;
        else if (hasTrait(rare))
            return ItemRarity.RARE;
        else if (hasTrait(uncommon))
            return ItemRarity.UNCOMMON;
        else if (hasTrait(poor))
            return ItemRarity.POOR;
        else {
            traits.add(common);
            return ItemRarity.COMMON;
        }
    }

    private static class Props {
        static final String texture = "texture";
    }
}
