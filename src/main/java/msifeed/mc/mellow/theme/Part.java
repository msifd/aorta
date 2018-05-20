package msifeed.mc.mellow.theme;

import com.google.gson.*;

import javax.vecmath.Point2f;
import java.lang.reflect.Type;
import java.util.Arrays;

public class Part {
    public Point2f pos;
    public Point2f size = null;

    public Point2f[] patchesUV = null;
    public Point2f[] patchesSize = null;

    public static class PointAdapter implements JsonDeserializer<Point2f> {
        @Override
        public Point2f deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonArray())
                throw new JsonParseException("The Point2f should be an array value");
            final int[] array = context.deserialize(json, int[].class);
            if (array.length != 2)
                throw new JsonParseException("The Point2f should have exactly two numbers");
            return new Point2f(array[0], array[1]);
        }
    }

    public static class PartAdapter implements JsonDeserializer<Part> {
        @Override
        public Part deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonObject())
                throw new JsonParseException("The Part should be an object value");
            final JsonObject obj = json.getAsJsonObject();
            final Part part = new Part();

            part.pos = context.deserialize(obj.get("pos").getAsJsonArray(), Point2f.class);

            if (obj.has("size"))
                part.size = context.deserialize(obj.get("size"), Point2f.class);

            if (obj.has("9patch")) {
                final Point2f[] compact = context.deserialize(obj.get("9patch"), Point2f[].class);
                if (compact.length != 3)
                    throw new JsonParseException("The Part's 9-patch should have exactly three points");
                part.patchesSize = new Point2f[9];
                part.patchesUV = new Point2f[9];
                for (int i = 0; i < 9; i++) {
                    final int u = Arrays.stream(compact).limit(i % 3).mapToInt(value -> (int) value.x).sum();
                    final int v = Arrays.stream(compact).limit(i / 3).mapToInt(value -> (int) value.y).sum();
                    part.patchesUV[i] = new Point2f(part.pos.x + u, part.pos.y + v);
                    part.patchesSize[i] = new Point2f(compact[i % 3].x, compact[i / 3].y);
                }
            }
            return part;
        }
    }
}
