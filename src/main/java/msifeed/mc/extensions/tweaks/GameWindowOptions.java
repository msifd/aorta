package msifeed.mc.extensions.tweaks;

import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import msifeed.mc.sys.config.ConfigBuilder;
import msifeed.mc.sys.config.ConfigManager;
import msifeed.mc.sys.config.JsonConfig;
import org.lwjgl.opengl.Display;

public class GameWindowOptions {
    private final TypeToken<Content> configContentType = new TypeToken<Content>() {};
    private JsonConfig<Content> config = ConfigBuilder.of(configContentType, "window.json").create();

    public void preInit() {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END)
            setTitle();
    }

    private void setTitle() {
        if (!config.valid())
            return;
        final String title = config.get().title;
        if (!Display.getTitle().equals(title))
            Display.setTitle(title);
    }

    public static class Content {
        public String title = "Morgana";
    }
}
