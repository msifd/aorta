package msifeed.mc.more.crabs.action.parser;

import com.google.gson.*;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.action.effects.Effect;

import java.lang.reflect.Type;

public class BuffJsonAdapter implements JsonSerializer<Buff>, JsonDeserializer<Buff> {
    @Override
    public JsonElement serialize(Buff src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.encode());
    }

    @Override
    public Buff deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive() && !json.getAsJsonPrimitive().isString())
            throw new JsonParseException("Buff expected simple string, got " + json.getClass().getSimpleName());

        final String buffSrc = json.getAsJsonPrimitive().getAsString();
        final Effect effect;
        try {
            effect = EffectStringParser.parseEffect(json.getAsJsonPrimitive().getAsString());
        } catch (RuntimeException e) {
            throw new JsonParseException("Got error while parsing buff: " + e.getMessage());
        }

        if (!(effect instanceof Buff))
            throw new JsonParseException("Effect is not a buff! Source: " + buffSrc);

        return (Buff) effect;
    }
}
