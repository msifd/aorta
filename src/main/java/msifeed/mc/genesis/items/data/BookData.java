package msifeed.mc.genesis.items.data;

import com.google.gson.JsonObject;
import msifeed.mc.sys.utils.JsonUtils;

public class BookData {
    public String index;

    public BookData(JsonObject json) {
        JsonUtils.consumeString(json, "index", s -> index = s);
        if (index == null)
            throw new RuntimeException("Book should have `index` field!");
    }
}
