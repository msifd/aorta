package msifeed.mc.aorta.tweaks;

import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.sys.config.ConfigEvent;
import msifeed.mc.aorta.sys.config.ConfigManager;
import msifeed.mc.aorta.sys.config.ConfigMode;
import msifeed.mc.aorta.sys.config.JsonConfig;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.Display;

public enum GameWindowOptions {
    INSTANCE;

    private final TypeToken<Content> configContentType = new TypeToken<Content>() {};
    private JsonConfig<Content> config = ConfigManager.getConfig(ConfigMode.CLIENT, configContentType, "window.json");


    public static void preInit() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    @SubscribeEvent
    public void onConfigUpdated(ConfigEvent.AfterUpdate event) {
        Display.setTitle(config.get().title);
        MinecraftForge.EVENT_BUS.unregister(INSTANCE);
    }

    public static class Content {
        public String title = "Ortega";
    }
}
