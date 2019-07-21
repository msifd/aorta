package msifeed.mc.aorta.environment;

import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.sys.config.ConfigEvent;
import msifeed.mc.aorta.sys.config.ConfigManager;
import msifeed.mc.aorta.sys.config.JsonConfig;
import net.minecraft.command.CommandHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import java.util.HashMap;

public enum EnvironmentManager {
    INSTANCE;

    private final TypeToken<HashMap<Integer, WorldEnv>> configContentType = new TypeToken<HashMap<Integer, WorldEnv>>() {};
    private final JsonConfig<HashMap<Integer, WorldEnv>> config = ConfigManager.getSyncConfig(configContentType, "world_env.json");

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
    public void onWorldLoad(WorldEvent.Load event) {
        loadEnvData(event.world);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onWorldSave(WorldEvent.Save event) {
        saveEnvData(event.world);
    }

    @SubscribeEvent
    public void beforeConfigUpdated(ConfigEvent.BeforeUpdate event) {
        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server == null)
            return;
        for (WorldServer ws : server.worldServers)
            saveEnvData(ws.provider.worldObj);
    }

    @SubscribeEvent
    public void afterConfigUpdated(ConfigEvent.AfterUpdate event) {
        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server == null)
            return;
        for (WorldServer ws : server.worldServers)
            loadEnvData(ws.provider.worldObj);
    }

    private void saveEnvData(World world) {
        final WorldEnv env = getEnv(world.provider.dimensionId);
        world.perWorldStorage.setData(WorldEnvMapData.DATA_NAME, new WorldEnvMapData(env));
    }

    private void loadEnvData(World world) {
        final WorldEnvMapData data = (WorldEnvMapData) world.perWorldStorage.loadData(WorldEnvMapData.class, WorldEnvMapData.DATA_NAME);
        final WorldEnv env = getEnv(world.provider.dimensionId);
        env.load(data);
    }
}
