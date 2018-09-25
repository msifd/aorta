package msifeed.mc.aorta.config;

import com.google.common.eventbus.EventBus;
import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public enum ConfigManager implements IMessageHandler<ConfigSyncMessage, ConfigSyncMessage> {
    INSTANCE;

    public static File config_dir;
    public final EventBus eventbus = new EventBus();
    static final Logger logger = LogManager.getLogger("Aorta.Config");
    private final SimpleNetworkWrapper network = new SimpleNetworkWrapper("aorta.config");

    public static <T> JsonConfig<T> getConfig(ConfigMode mode, TypeToken<T> t, String filename) {
        JsonConfig<T> handler = new JsonConfig<>(mode, t, filename);
        INSTANCE.eventbus.register(handler);
        return handler;
    }

    public static void init(FMLPreInitializationEvent event) {
        config_dir = new File(event.getModConfigurationDirectory(), "aorta");
        config_dir.mkdirs();

        INSTANCE.network.registerMessage(INSTANCE, ConfigSyncMessage.class, 0, Side.CLIENT);
        FMLCommonHandler.instance().bus().register(INSTANCE);

        reloadConfig();
    }

    public static void reloadConfig() {
        INSTANCE.eventbus.post(new ConfigEvent.Reload());
        INSTANCE.eventbus.post(new ConfigEvent.UpdateDone());
    }

    public void broadcastConfig() {
        network.sendToAll(collectOverrideMessage());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        network.sendTo(collectOverrideMessage(), (EntityPlayerMP) event.player);
    }

    @Override
    public ConfigSyncMessage onMessage(ConfigSyncMessage message, MessageContext ctx) {
        ConfigEvent.Override event = new ConfigEvent.Override();
        event.configs = message.configs;
        eventbus.post(event);
        eventbus.post(new ConfigEvent.UpdateDone());
        return null;
    }

    private ConfigSyncMessage collectOverrideMessage() {
        ConfigEvent.Collect event = new ConfigEvent.Collect();
        eventbus.post(event);

        ConfigSyncMessage msg = new ConfigSyncMessage();
        msg.configs = event.configs;
        return msg;
    }
}
