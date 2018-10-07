package msifeed.mc.aorta.genesis.items;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import msifeed.mc.aorta.genesis.GenesisTrait;
import msifeed.mc.aorta.genesis.GenesisUnit;
import msifeed.mc.aorta.genesis.JsonUtils;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import static msifeed.mc.aorta.genesis.GenesisTrait.*;

public class ItemGenesisUnit extends GenesisUnit {
    public String title;
    public String[] desc;
    public LinkedHashMap<String, String> values = new LinkedHashMap<>();
    public String texture;
    public ItemRarity rarity;

    public ItemGenesisUnit(JsonObject json, HashSet<GenesisTrait> traits) {
        super(json, traits);
        title = JsonUtils.getOptString(json, Props.title).orElse(null);
        desc = JsonUtils.getOptString(json, Props.desc).map(ItemGenesisUnit::parseDescription).orElse(null);
        loadDescriptionValues(json);
        texture = JsonUtils.getOptString(json, Props.texture).orElse(null);

        rarity = getRarity();
        if (rarity == ItemRarity.COMMON)
            traits.add(common);
    }

    private void loadDescriptionValues(JsonObject json) {
        if (!json.has(Props.values) || !json.get(Props.values).isJsonObject())
            return;

        final JsonObject values = json.get(Props.values).getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : values.entrySet()) {
            if (!entry.getValue().isJsonPrimitive() || !entry.getValue().getAsJsonPrimitive().isString())
                continue;
            this.values.put(entry.getKey(), entry.getValue().getAsString());
        }
    }

    private static String[] parseDescription(String raw) {
        return raw.split("\n");
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
        else
            return ItemRarity.COMMON;
    }

    private static class Props {
        static final String title = "title";
        static final String desc = "description";
        static final String values = "values";
        static final String texture = "texture";
    }
}
