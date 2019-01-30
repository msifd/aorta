package msifeed.mc.aorta.environment;

import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import msifeed.mc.aorta.config.ConfigManager;
import msifeed.mc.aorta.config.ConfigMode;
import msifeed.mc.aorta.config.JsonConfig;
import msifeed.mc.aorta.environment.client.WeatherRenderer;
import net.minecraft.command.CommandHandler;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenEnd;
import net.minecraft.world.biome.BiomeGenHell;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;

public enum EnvironmentManager {
    INSTANCE;

    private WeatherRenderer weatherRenderer = new WeatherRenderer();

    private final TypeToken<HashMap<Integer, WorldEnv>> configContentType = new TypeToken<HashMap<Integer, WorldEnv>>() {};
    private final JsonConfig<HashMap<Integer, WorldEnv>> config = ConfigManager.getConfig(ConfigMode.CLIENT, configContentType, "world_env.json");

    @SidedProxy(
            serverSide = "msifeed.mc.aorta.environment.EnvHandler",
            clientSide = "msifeed.mc.aorta.environment.client.EnvHandlerClient"
    )
    private static EnvHandler envHandler;

    public static void init() {
        FMLCommonHandler.instance().bus().register(INSTANCE);
        FMLCommonHandler.instance().bus().register(envHandler);
        MinecraftForge.EVENT_BUS.register(envHandler);
        MinecraftForge.EVENT_BUS.register(INSTANCE.weatherRenderer);
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

    public static void syncEnv() {
        ConfigManager.INSTANCE.broadcast();
    }

    private static boolean isRegularBiome(BiomeGenBase biome) {
        return !(biome instanceof BiomeGenHell || biome instanceof BiomeGenEnd);
    }
}
