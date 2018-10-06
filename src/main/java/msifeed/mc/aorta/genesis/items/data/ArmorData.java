package msifeed.mc.aorta.genesis.items.data;

import com.google.gson.JsonObject;
import msifeed.mc.aorta.genesis.JsonUtils;

public class ArmorData {
    private static final String EMPTY_ARMOR_TEXTURE = "aorta:textures/models/armor/empty_armor";

    public String texture;

    public ArmorData(JsonObject json) {
        texture = JsonUtils.getOptString(json, Props.texture).orElse(EMPTY_ARMOR_TEXTURE);
    }

    private static class Props {
        static final String texture = "armor_texture";
    }
}
