package msifeed.mc.aorta.genesis.blocks.data;

import com.google.gson.JsonObject;
import msifeed.mc.aorta.genesis.JsonUtils;

public class TrapData {
    public String message = "";
    public int radius = 15;
    public boolean destroy = false;

    public TrapData(JsonObject json) {
        JsonUtils.consumeString(json, "message", v -> message = v);
        JsonUtils.consumeInt(json, "radius", v -> radius = v);
        JsonUtils.consumeBool(json, "destroy", v -> destroy = v);
    }
}
