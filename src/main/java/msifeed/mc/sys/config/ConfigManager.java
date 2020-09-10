package msifeed.mc.sys.config;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import msifeed.mc.Bootstrap;
import msifeed.mc.more.More;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public enum ConfigManager {
    INSTANCE;

    private File configDir;
    private final HashMap<String, JsonConfig<?>> handlers = new HashMap<>();

    public static void preInit(FMLPreInitializationEvent event) {
        INSTANCE.configDir = new File(event.getModConfigurationDirectory(), Bootstrap.MODID);
        INSTANCE.configDir.mkdirs();

        FMLCommonHandler.instance().bus().register(INSTANCE);
        More.RPC.register(ConfigRpc.INSTANCE);
    }

    public static void load() {
        for (JsonConfig<?> cfg : INSTANCE.handlers.values())
            cfg.load();
    }

    public static boolean reload() {
        final Map<JsonConfig, Object> newValues = new HashMap<>();

        try {
            for (JsonConfig<?> cfg : INSTANCE.handlers.values())
                newValues.put(cfg, cfg.read());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        MinecraftForge.EVENT_BUS.post(new ConfigEvent.BeforeUpdate());
        newValues.forEach(JsonConfig::set);
        MinecraftForge.EVENT_BUS.post(new ConfigEvent.AfterUpdate());

        return true;
    }

    public static void save() {
        for (JsonConfig<?> c : INSTANCE.handlers.values())
            c.save();
    }

    public static void broadcast() {
        for (JsonConfig<?> cfg : INSTANCE.handlers.values()) {
            if (cfg.isSyncable())
                ConfigRpc.broadcast(cfg);
        }
    }

    static File getConfigFile(String filename) {
        return new File(INSTANCE.configDir, filename);
    }

    static void register(JsonConfig<?> jsonConfig) {
        INSTANCE.handlers.put(jsonConfig.getFilename(), jsonConfig);
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        for (JsonConfig<?> cfg : INSTANCE.handlers.values()) {
            if (cfg.isSyncable())
                ConfigRpc.sendTo((EntityPlayerMP) event.player, cfg);
        }
    }

    void applySyncConfig(String filename, String text) {
        final JsonConfig<?> cfg = handlers.get(filename);
        if (cfg != null && cfg.isSyncable())
            cfg.fromJson(text);
    }

    private HashMap<String, String> collectConfigs() {
        final HashMap<String, String> r = new HashMap<>();
        for (JsonConfig<?> c : handlers.values())
            r.put(c.getFilename(), c.toJson());
        return r;
    }
}
