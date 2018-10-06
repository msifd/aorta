package msifeed.mc.aorta.genesis;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;

public class JsonUtils {
    public static Optional<String> getOptString(JsonObject obj, String key) {
        if (obj.has(key)) {
            final JsonElement t = obj.get(key);
            if (t.isJsonPrimitive() && t.getAsJsonPrimitive().isString()) {
                return Optional.of(t.getAsJsonPrimitive().getAsString());
            }
        }
        return Optional.empty();
    }
}
