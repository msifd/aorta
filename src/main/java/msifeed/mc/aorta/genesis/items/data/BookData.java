package msifeed.mc.aorta.genesis.items.data;

import com.google.gson.JsonObject;
import msifeed.mc.aorta.genesis.JsonUtils;

public class BookData {
    public String index;

    public BookData(JsonObject json) {
        index = JsonUtils.getOptString(json, Props.index).orElseThrow(() -> new RuntimeException("Book should have `index` field!"));
    }

    private static class Props {
        static final String index = "index";
    }
}
