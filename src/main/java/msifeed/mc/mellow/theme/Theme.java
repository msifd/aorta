package msifeed.mc.mellow.theme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.ResourceLocation;

import javax.vecmath.Point2f;
import java.util.HashMap;

public class Theme {
    public transient ResourceLocation sprite;
    public HashMap<String, Part> parts;
    public transient HashMap<String, Integer> colors = new HashMap<>();

    @SerializedName("colors")
    private HashMap<String, String> colorsStr;

    public static Theme load(ResourceLocation sprite, String metaJson) {
        final Gson gson = (new GsonBuilder())
                .registerTypeAdapter(Point2f.class, new Part.PointAdapter())
                .registerTypeAdapter(Part.class, new Part.PartAdapter())
                .create();

        final Theme theme = gson.fromJson(metaJson, Theme.class);
        theme.sprite = sprite;
        for (HashMap.Entry<String, String> e : theme.colorsStr.entrySet())
            theme.colors.put(e.getKey(), Integer.parseInt(e.getValue(), 16));

        return theme;
    }
}
