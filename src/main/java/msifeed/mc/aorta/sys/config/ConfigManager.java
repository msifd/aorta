package msifeed.mc.aorta.sys.config;

import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.aorta.sys.rpc.Rpc;
import msifeed.mc.aorta.sys.rpc.RpcMethod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public enum ConfigManager {
    INSTANCE;

    private static final String broadcastRpc = "aorta:config.broadcast";
    static File config_dir;
    private ArrayList<JsonConfig> handlers = new ArrayList<>();

    public static <T> JsonConfig<T> getConfig(ConfigMode mode, TypeToken<T> t, String filename) {
        JsonConfig<T> handler = new JsonConfig<>(mode, t, filename);
        INSTANCE.handlers.add(handler);
        return handler;
    }

    public static void init(FMLPreInitializationEvent event) {
        config_dir = new File(event.getModConfigurationDirectory(), "aorta");
        config_dir.mkdirs();

        FMLCommonHandler.instance().bus().register(INSTANCE);
        reload();
    }

    public static void reload() {
        final HashMap<String, String> backup = INSTANCE.collectConfigs();
        try {
            for (JsonConfig c : INSTANCE.handlers)
                c.reload();
            MinecraftForge.EVENT_BUS.post(new ConfigEvent.Updated());
            save();
        } catch (Exception e) {
            for (JsonConfig c : INSTANCE.handlers)
                c.fromJson(backup.get(c.getFilename()));
        }
    }

    public static void broadcast() {
        Rpc.sendToAll(broadcastRpc, INSTANCE.collectConfigs());
    }

    public static void save() {
        for (JsonConfig c : INSTANCE.handlers)
            c.save();
    }

    @RpcMethod(broadcastRpc)
    public void onBroadcast(MessageContext ctx, HashMap<String, String> configs) {
        if (ctx.side.isServer())
            return;
        for (JsonConfig c : INSTANCE.handlers)
            c.fromJson(configs.get(c.getFilename()));
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Rpc.sendTo((EntityPlayerMP) event.player, broadcastRpc, collectConfigs());
    }

    private HashMap<String, String> collectConfigs() {
        final HashMap<String, String> r = new HashMap<>();
        for (JsonConfig c : INSTANCE.handlers)
           r.put(c.getFilename(), c.toJson());
        return r;
    }
}
