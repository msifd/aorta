package msifeed.mc.aorta.genesis;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;
import java.util.function.Consumer;

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

    public static void consumeFloat(JsonObject json, String name, Consumer<Float> consumer) {
        if (!json.has(name) || !json.get(name).isJsonPrimitive() || !json.get(name).getAsJsonPrimitive().isNumber())
            return;
        consumer.accept(json.get(name).getAsJsonPrimitive().getAsFloat());
    }
}
