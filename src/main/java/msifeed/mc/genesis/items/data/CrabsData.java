package msifeed.mc.genesis.items.data;

import com.google.gson.JsonObject;
import msifeed.mc.sys.utils.JsonUtils;

public class CrabsData {
    public String action = null;

    public CrabsData() {
    }

    public CrabsData(JsonObject json) {
        JsonUtils.consumeString(json, "action", s -> action = s);
    }
}
