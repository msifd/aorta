package msifeed.mc.aorta.sys.attributes;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class SyncAttrHandlerClient extends SyncAttrHandler {
    @Override
    public IMessage onMessage(SyncAttrMessage message, MessageContext ctx) {
        final World clientWorld = FMLClientHandler.instance().getWorldClient();
        final World serverWorld = DimensionManager.getWorld(clientWorld.provider.dimensionId);
        setAttr(clientWorld, message);
        if (serverWorld != null)
            setAttr(serverWorld, message);
        return null;
    }

    private static void setAttr(World w, SyncAttrMessage message) {
        final Entity e = w.getEntityByID(message.entityId);
        if (e != null) {
            final EntityAttribute attribute = AttributeHandler.INSTANCE.attributes.get(message.attrName);
            if (attribute != null)
                attribute.fromNBT(e, message.compound);
        } else {
            System.err.println("Missing sync entity!");
        }
    }
}
