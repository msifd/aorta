package msifeed.mc.aorta.genesis.items;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import msifeed.mc.aorta.genesis.GenesisTrait;
import msifeed.mc.aorta.genesis.GenesisUnit;

import java.util.HashSet;

public class ItemGenesisUnit extends GenesisUnit {
    public String texture = null;

    public ItemGenesisUnit(JsonObject json, HashSet<GenesisTrait> traits) {
        super(json, traits);

        if (json.has(Props.texture)) {
            final JsonElement tEl = json.get(Props.texture);
            if (tEl.isJsonPrimitive() && tEl.getAsJsonPrimitive().isString()) {
                texture = tEl.getAsJsonPrimitive().getAsString();
            }
        }
    }

    private static class Props {
        static final String texture = "texture";
    }
}
