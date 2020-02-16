package msifeed.mc.sys.config.adapters;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.ZoneId;

public class ZoneIdAdapter implements JsonSerializer<ZoneId>, JsonDeserializer<ZoneId> {
    @Override
    public JsonElement serialize(ZoneId src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public ZoneId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//        if (json.isJsonNull())
//            return ZoneOffset.UTC;
//        final String arg = json.getAsJsonPrimitive().getAsString();
//        if (arg.isEmpty())
//            return ZoneOffset.UTC;
//        return ZoneId.of(arg);
        return ZoneId.of(json.getAsJsonPrimitive().getAsString());
    }
}
