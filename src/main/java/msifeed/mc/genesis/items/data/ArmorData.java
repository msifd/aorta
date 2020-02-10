package msifeed.mc.genesis.items.data;

import com.google.gson.JsonObject;
import msifeed.mc.Bootstrap;
import msifeed.mc.sys.utils.JsonUtils;

public class ArmorData {
    private static final String EMPTY_ARMOR_TEXTURE = Bootstrap.MODID + ":textures/models/armor/empty_armor";

    public String texture = EMPTY_ARMOR_TEXTURE;

    public ArmorData(JsonObject json) {
        JsonUtils.consumeString(json, "armor_texture", s -> texture = s);
    }
}
