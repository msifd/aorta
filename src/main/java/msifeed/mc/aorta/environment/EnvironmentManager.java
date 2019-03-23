package msifeed.mc.aorta.environment;

import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.sys.config.ConfigManager;
import msifeed.mc.aorta.sys.config.ConfigMode;
import msifeed.mc.aorta.sys.config.JsonConfig;
import net.minecraft.command.CommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import java.util.HashMap;

public enum EnvironmentManager {
    INSTANCE;

    private final TypeToken<HashMap<Integer, WorldEnv>> configContentType = new TypeToken<HashMap<Integer, WorldEnv>>() {};
    private final JsonConfig<HashMap<Integer, WorldEnv>> config = ConfigManager.getConfig(ConfigMode.SERVER, configContentType, "world_env.json");

    @SidedProxy(
            serverSide = "msifeed.mc.aorta.environment.EnvHandler",
            clientSide = "msifeed.mc.aorta.environment.client.EnvHandlerClient"
    )
    private static EnvHandler envHandler;

    public static void init() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
        envHandler.init();
    }

    public static void registerCommands(CommandHandler commandHandler) {
        commandHandler.registerCommand(new EnvironmentCommand());
    }

    public static WorldEnv getEnv(int dim) {
        WorldEnv s = INSTANCE.config.get().get(dim);
        if (s == null) {
            s = new WorldEnv();
            INSTANCE.config.get().put(dim, s);
        }
        return s;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private void onWorldLoad(WorldEvent.Load event) {
        final WorldEnvMapData data = (WorldEnvMapData) event.world.mapStorage.loadData(WorldEnvMapData.class, WorldEnvMapData.DATA_NAME);
        final WorldEnv env = getEnv(event.world.provider.dimensionId);
        env.load(data);
    }
}
