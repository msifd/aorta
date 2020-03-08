package msifeed.mc.sys.attributes;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import msifeed.mc.Bootstrap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.HashMap;

public class AttributeHandler {
    static final AttributeHandler INSTANCE = new AttributeHandler();
    static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Bootstrap.MODID + ".attr");

    final HashMap<String, EntityAttribute> attributes = new HashMap<>();

    @SidedProxy(
            serverSide = "msifeed.mc.sys.attributes.SyncAttrHandler",
            clientSide = "msifeed.mc.sys.attributes.SyncAttrHandlerClient"
    )
    private static SyncAttrHandler syncAttrHandler;

    public static void init() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
        CHANNEL.registerMessage(syncAttrHandler, SyncAttrMessage.class, 0, Side.CLIENT);
    }

    public static void registerAttribute(EntityAttribute attribute) {
        INSTANCE.attributes.put(attribute.getName(), attribute);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityConstruct(EntityEvent.EntityConstructing e) {
        for (EntityAttribute a : attributes.values())
            a.onEntityConstruct(e);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityJoinWorld(EntityJoinWorldEvent e) {
        for (EntityAttribute a : attributes.values())
            a.onEntityJoinWorld(e);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onPlayerStartedTracking(PlayerEvent.StartTracking e) {
        for (EntityAttribute a : attributes.values())
            a.onPlayerStartedTracking(e);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onClonePlayer(PlayerEvent.Clone e) {
        for (EntityAttribute a : attributes.values())
            a.onClonePlayer(e);
    }
}
