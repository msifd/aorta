package msifeed.mc.more.crabs.action.parser;

import com.google.gson.*;
import msifeed.mc.more.crabs.action.Action;
import msifeed.mc.more.crabs.action.ActionTag;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.action.effects.Effect;
import msifeed.mc.more.crabs.action.effects.EffectsRegistry;
import msifeed.mc.more.crabs.action.effects.Score;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class BuffJsonAdapter implements JsonSerializer<Buff>, JsonDeserializer<Buff> {
    @Override
    public JsonElement serialize(Buff src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
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
