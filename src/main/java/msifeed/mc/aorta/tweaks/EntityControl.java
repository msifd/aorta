package msifeed.mc.aorta.tweaks;

import com.google.common.eventbus.Subscribe;
import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.config.ConfigEvent;
import msifeed.mc.aorta.config.ConfigManager;
import msifeed.mc.aorta.config.ConfigMode;
import msifeed.mc.aorta.config.JsonConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntityControl {
    private static Logger logger = LogManager.getLogger("Aorta.EntityControl");
    private final TypeToken<ArrayList<ConfigContent>> configContentType = new TypeToken<ArrayList<ConfigContent>>() {};
    private JsonConfig<ArrayList<ConfigContent>> config = ConfigManager.getConfig(ConfigMode.CLIENT, configContentType, "entity_control.json");

    public EntityControl() {
        ConfigManager.INSTANCE.eventbus.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Subscribe
    public void onReloadDone(ConfigEvent.UpdateDone event) {
        compileConfig();

        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server == null || server.isServerStopped())
            return;

        for (WorldServer ws : server.worldServers) {
            for (Entity e : (List<Entity>) ws.loadedEntityList) {
                if (isBlacklisted(ws, e))
                    ws.removeEntity(e);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityJoin(EntityJoinWorldEvent event) {
        if (!(event.entity instanceof EntityLivingBase))
            return;

        if (isBlacklisted(event.world, event.entity)) {
            event.setCanceled(true);
        }
    }

    private void compileConfig() {
        for (ConfigContent c : config.get())
            c.compile();
    }

    private boolean isBlacklisted(World world, Entity entity) {
        final Class entityClass = entity.getClass();
        final String worldName = world.getWorldInfo().getWorldName();

        for (final ConfigContent entry : config.get()) {
            if (!entry.dimensions.isEmpty() && !entry.dimensions.contains(worldName))
                continue;
            for (final Class<?> c : entry.classes) {
                if (c.isAssignableFrom(entityClass))
                    return true;
            }
        }

        return false;
    }

    static class ConfigContent {
        Set<String> entities = new HashSet<>();
        Set<String> dimensions = new HashSet<>();
        transient Set<Class> classes = new HashSet<>();

        void compile() {
            classes.clear();
            for (String s : entities) {
                if (s == null)
                    continue;
                try {
                    classes.add(Class.forName(s));
                } catch (ClassNotFoundException e) {
                    logger.error("Unknown class '%s'.", s);
                }
            }
        }
    }
}
