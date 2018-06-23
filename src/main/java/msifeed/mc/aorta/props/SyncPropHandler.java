package msifeed.mc.aorta.props;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class SyncPropHandler implements IMessageHandler<SyncPropMessage, IMessage> {

    @Override
    public IMessage onMessage(SyncPropMessage message, MessageContext ctx) {
        final World w = ctx.getServerHandler().playerEntity.worldObj;
        setProp(w, message);
        return null;
    }

    static void setProp(World w, SyncPropMessage message) {
        final Entity e = w.getEntityByID(message.entityId);
        if (e != null) {
            final IExtendedEntityProperties prop = e.getExtendedProperties(message.propName);
            if (prop != null) {
                prop.loadNBTData(message.compound);
                if (prop instanceof ExtProp)
                    ((ExtProp) prop).sync(w, e);
            }
        } else {
            System.err.println("Missing sync entity!");
        }
    }

}
