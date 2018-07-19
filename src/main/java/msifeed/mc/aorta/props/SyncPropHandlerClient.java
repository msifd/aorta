package msifeed.mc.aorta.props;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.IExtendedEntityProperties;

public class SyncPropHandlerClient extends SyncPropHandler {
    @Override
    public IMessage onMessage(SyncPropMessage message, MessageContext ctx) {
        final World clientWorld = FMLClientHandler.instance().getWorldClient();
        final World serverWorld = DimensionManager.getWorld(clientWorld.provider.dimensionId);
        setProp(clientWorld, message);
        if (serverWorld != null)
            setProp(serverWorld, message);
        return null;
    }

    private static void setProp(World w, SyncPropMessage message) {
        final Entity e = w.getEntityByID(message.entityId);
        if (e != null) {
            final IExtendedEntityProperties prop = e.getExtendedProperties(message.propName);
            if (prop != null) {
                prop.loadNBTData(message.compound);
            }
        } else {
            System.err.println("Missing sync entity!");
        }
    }
}
