package msifeed.mc.aorta.genesis.blocks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import msifeed.mc.aorta.genesis.GenesisTrait;
import msifeed.mc.aorta.genesis.GenesisUnit;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BlockGenesisUnit extends GenesisUnit {
    public String textureString = null;
    public List<String> textureArray = null;
    public int[] textureLayout = null;

    public BlockGenesisUnit(JsonObject json, HashSet<GenesisTrait> traits) {
        super(json, traits);

        if (json.has(Props.texture)) {
            final JsonElement tEl = json.get(Props.texture);
            if (tEl.isJsonPrimitive() && tEl.getAsJsonPrimitive().isString()) {
                textureString = tEl.getAsJsonPrimitive().getAsString();
            } else if (tEl.isJsonArray()) {
                textureArray = StreamSupport.stream(tEl.getAsJsonArray().spliterator(), false)
                        .limit(6)
                        .map(e -> e.getAsJsonPrimitive().getAsString())
                        .collect(Collectors.toList());
            }
        }

        if (json.has(Props.textureLayout)) {
            final JsonElement tlEl = json.get(Props.textureLayout);
            if (tlEl.isJsonPrimitive() && tlEl.getAsJsonPrimitive().isString()) {
                final String layoutStr = tlEl.getAsJsonPrimitive().getAsString();
                textureLayout = layoutStr.codePoints()
                        .limit(6)
                        .map(Character::getNumericValue)
                        .map(i -> Math.min(i, 5)) // [0, 5]
                        .toArray();
            }
        }
    }

    private static class Props {
        static final String texture = "texture";
        static final String textureLayout = "texture_layout";
    }
}
