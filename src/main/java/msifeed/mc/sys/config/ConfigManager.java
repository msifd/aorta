package msifeed.mc.sys.config;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import msifeed.mc.Bootstrap;
import msifeed.mc.sys.rpc.Rpc;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public enum ConfigManager {
    INSTANCE;

    private File configDir;
    private ArrayList<JsonConfig> handlers = new ArrayList<>();

    public static void preInit(FMLPreInitializationEvent event) {
        INSTANCE.configDir = new File(event.getModConfigurationDirectory(), Bootstrap.MODID);
        INSTANCE.configDir.mkdirs();

        FMLCommonHandler.instance().bus().register(INSTANCE);
        Rpc.register(ConfigRpc.INSTANCE);
    }

    public static boolean reload() {
        final HashMap<String, String> backup = INSTANCE.collectConfigs();
        try {
            MinecraftForge.EVENT_BUS.post(new ConfigEvent.BeforeUpdate());
            for (JsonConfig c : INSTANCE.handlers)
                c.reload();
            MinecraftForge.EVENT_BUS.post(new ConfigEvent.AfterUpdate());
            save();
        } catch (Exception e) {
            e.printStackTrace();
            for (JsonConfig c : INSTANCE.handlers)
                c.fromJson(backup.get(c.getFilename()));
            return false;
        }

        return true;
    }

    public static void save() {
        for (JsonConfig c : INSTANCE.handlers)
            c.save();
    }

    public static void broadcast() {
        ConfigRpc.broadcast(INSTANCE.collectSyncConfigs());
    }

    static File getConfigFile(String filename) {
        return new File(INSTANCE.configDir, filename);
    }

    static void register(JsonConfig jsonConfig) {
        INSTANCE.handlers.add(jsonConfig);
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ConfigRpc.sendTo((EntityPlayerMP) event.player, collectSyncConfigs());
    }

    void onConfigSync(Map<String, String> configs) {
        handlers.stream().filter(JsonConfig::isSyncable).forEach(c -> {
            c.fromJson(configs.get(c.getFilename()));
        });
    }

    private HashMap<String, String> collectConfigs() {
        final HashMap<String, String> r = new HashMap<>();
        for (JsonConfig c : handlers)
            r.put(c.getFilename(), c.toJson());
        return r;
    }

    private HashMap<String, String> collectSyncConfigs() {
        final HashMap<String, String> map = new HashMap<>();
        for (JsonConfig c : handlers)
            if (c.isSyncable())
                map.put(c.getFilename(), c.toJson());
        return map;
    }
}
