package msifeed.mc.mellow.theme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import msifeed.mc.mellow.utils.Point;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class Theme {
    public HashMap<String, Part> parts;
    public transient HashMap<String, Integer> colors = new HashMap<>();

    @SerializedName("colors")
    private HashMap<String, String> colorsHex; // Using to read hex values instead of ints

    public static Theme load(ResourceLocation sprite, String metaJson) {
        final Gson gson = (new GsonBuilder())
                .registerTypeAdapter(Point.class, new Part.PointAdapter())
                .registerTypeAdapter(Part.class, new Part.PartAdapter())
                .create();

        final Theme theme = gson.fromJson(metaJson, Theme.class);
        for (Part p : theme.parts.values())
            p.sprite = sprite;
        for (HashMap.Entry<String, String> e : theme.colorsHex.entrySet())
            theme.colors.put(e.getKey(), Integer.parseInt(e.getValue(), 16));

        return theme;
    }
}
