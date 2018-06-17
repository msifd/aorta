package msifeed.mc.mellow.theme;

import com.google.gson.*;
import msifeed.mc.mellow.utils.Point;

import java.lang.reflect.Type;
import java.util.Arrays;

public class Part {
    public Point pos;
    public Point size = null;

    public Point[] slicesUV = null;
    public Point[] slicesSize = null;

    public static class PointAdapter implements JsonDeserializer<Point> {
        @Override
        public Point deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonArray())
                throw new JsonParseException("The Point should be an array value");
            final int[] array = context.deserialize(json, int[].class);
            if (array.length != 2)
                throw new JsonParseException("The Point should have exactly two numbers");
            return new Point(array[0], array[1]);
        }
    }

    public static class PartAdapter implements JsonDeserializer<Part> {
        @Override
        public Part deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonObject())
                throw new JsonParseException("The Part should be an object value");
            final JsonObject obj = json.getAsJsonObject();
            final Part part = new Part();

            part.pos = context.deserialize(obj.get("pos").getAsJsonArray(), Point.class);

            if (obj.has("size"))
                part.size = context.deserialize(obj.get("size"), Point.class);

            if (obj.has("9slice")) {
                final Point[] compact = context.deserialize(obj.get("9slice"), Point[].class);
                if (compact.length != 3)
                    throw new JsonParseException("The Part's 9-Slice should have exactly three points");
                part.slicesSize = new Point[9];
                part.slicesUV = new Point[9];
                for (int i = 0; i < 9; i++) {
                    final int u = Arrays.stream(compact).limit(i % 3).mapToInt(value -> (int) value.x).sum();
                    final int v = Arrays.stream(compact).limit(i / 3).mapToInt(value -> (int) value.y).sum();
                    part.slicesUV[i] = new Point(part.pos.x + u, part.pos.y + v);
                    part.slicesSize[i] = new Point(compact[i % 3].x, compact[i / 3].y);
                }
            }
            return part;
        }
    }
}
