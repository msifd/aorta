package msifeed.mc.aorta.props;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.aorta.network.Networking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IExtendedEntityProperties;

public class SyncProp implements IMessageHandler<MessageSyncProp, IMessage> {
    public static void sync(EntityPlayerMP playerMP, Entity entity, ISyncableExtProp prop) {
        final MessageSyncProp msg = new MessageSyncProp();
        msg.entityId = entity.getEntityId();
        msg.propName = prop.getName();
        msg.compound = new NBTTagCompound();
        prop.saveNBTData(msg.compound);

        Networking.CHANNEL.sendTo(msg, playerMP);
    }

    public static void sync(World world, Entity entity, ISyncableExtProp prop) {
        if (!world.isRemote || !(world instanceof WorldServer))
            return;

        final MessageSyncProp msg = new MessageSyncProp(entity, prop);
        final EntityTracker tracker = ((WorldServer) world).getEntityTracker();
        for (EntityPlayer player : tracker.getTrackingPlayers(entity)) {
            Networking.CHANNEL.sendTo(msg, (EntityPlayerMP) player);
        }
    }

    @Override
    public IMessage onMessage(MessageSyncProp message, MessageContext ctx) {
        final Entity e = FMLClientHandler.instance().getWorldClient().getEntityByID(message.entityId);
        if (e != null) {
            final IExtendedEntityProperties prop = e.getExtendedProperties(message.propName);
            if (prop != null)
                prop.loadNBTData(message.compound);
        }
        return null;
    }
}
