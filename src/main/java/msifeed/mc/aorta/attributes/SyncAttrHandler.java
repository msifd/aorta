package msifeed.mc.aorta.attributes;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class SyncAttrHandler implements IMessageHandler<SyncAttrMessage, IMessage> {
    @Override
    public IMessage onMessage(SyncAttrMessage message, MessageContext ctx) {
        final World w = ctx.getServerHandler().playerEntity.worldObj;
        final Entity e = w.getEntityByID(message.entityId);
        if (e != null) {
            final EntityAttribute attribute = AttributeHandler.INSTANCE.attributes.get(message.attrName);
            if (attribute != null)
                attribute.fromNBT(e, message.compound);
        } else {
            System.err.println("Missing sync entity!");
        }
        return null;
    }
}
