package msifeed.mc.mellow;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.theme.Theme;
import net.minecraft.util.ResourceLocation;

import javax.vecmath.Point2f;

public class Mellow {
    //    public static EventBus EVENT_BUS = new EventBus();
    public static Theme THEME = null;

    public static void loadTheme(ResourceLocation sprite, String metaJson) {
        THEME = getGson().fromJson(metaJson, Theme.class);
        THEME.sprite = sprite;
    }

    private static Gson getGson() {
        return (new GsonBuilder())
                .registerTypeAdapter(Point2f.class, new Part.PointAdapter())
                .registerTypeAdapter(Part.class, new Part.PartAdapter())
                .create();
    }
}
