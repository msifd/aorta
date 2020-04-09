package msifeed.mc.more.crabs.action.parser;

import com.google.gson.*;
import msifeed.mc.more.crabs.action.Action;
import msifeed.mc.more.crabs.action.ActionTag;
import msifeed.mc.more.crabs.action.effects.Effect;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ActionJsonAdapter implements JsonSerializer<Action>, JsonDeserializer<Action> {
    @Override
    public JsonElement serialize(Action src, Type typeOfSrc, JsonSerializationContext context) {
        final RawAction raw = new RawAction();

        raw.id = src.id;
        raw.title = src.title;
        raw.tags = src.tags.stream().map(Enum::toString).collect(Collectors.toCollection(ArrayList::new));

        if (!src.target.isEmpty())
            raw.target = src.target.stream().map(Effect::toString).collect(Collectors.toCollection(ArrayList::new));
        if (!src.self.isEmpty())
            raw.self = src.self.stream().map(Effect::toString).collect(Collectors.toCollection(ArrayList::new));

        return context.serialize(raw);
    }

    @Override
    public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final RawAction raw = context.deserialize(json, RawAction.class);

        if (raw.id == null)
            throw new JsonParseException("Action has no `id` field!");
        if (raw.title == null)
            throw new JsonParseException("Action `" + raw.id + "` has no `title` field!");

        final Action action = new Action(raw.id, raw.title);

        if (raw.tags == null)
            throw new JsonParseException("Action `" + raw.id + "` has no `tags` field!");
//        if (raw.tags.isEmpty())
//            throw new JsonParseException("Action `" + raw.id + "` has no tags in `tags` field!");
        ActionTag type = null;
        for (String st : raw.tags) {
            try {
                final ActionTag at = ActionTag.valueOf(st.toLowerCase());
                if (at.isType()) {
                    if (type != null)
                        throw new JsonParseException("Action `" + raw.id + "` has has multiple type tags!");
                    type = at;
                }
                action.tags.add(at);
            } catch (IllegalArgumentException ignore) {
                throw new JsonParseException("Action `" + raw.id + "` has invalid tag `" + st + "`!");
            }
        }

        if (raw.self != null) {
            for (String es : raw.self) {
                try {
                    action.self.add(EffectStringParser.parseEffect(es));
                } catch (RuntimeException e) {
                    throw new JsonParseException("Action `" + raw.id + "` has invalid self effect. Src: '" + es + "'. Error: " + e.getMessage());
                }
            }
        }

        if (raw.target != null) {
            for (String es : raw.target) {
                try {
                    action.target.add(EffectStringParser.parseEffect(es));
                } catch (RuntimeException e) {
                    throw new JsonParseException("Action `" + raw.id + "` has invalid target effect. Error: " + e.getMessage());
                }
            }
        }

        return action;
    }

    static class RawAction {
        String id;
        String title;
        ArrayList<String> tags;
        ArrayList<String> self;
        ArrayList<String> target;
    }
}
