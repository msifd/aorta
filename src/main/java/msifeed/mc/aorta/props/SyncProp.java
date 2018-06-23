package msifeed.mc.aorta.props;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.aorta.network.Networking;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IExtendedEntityProperties;

public class SyncProp implements IMessageHandler<MessageSyncProp, IMessage> {

    public static void syncOne(EntityPlayerMP playerMP, Entity entity, SyncableExtProp prop) {
        final MessageSyncProp msg = new MessageSyncProp(entity, prop);
        Networking.CHANNEL.sendTo(msg, playerMP);
    }

    public static void syncWithTracking(World world, Entity entity, SyncableExtProp prop) {
        final MessageSyncProp msg = new MessageSyncProp(entity, prop);
        if (world instanceof WorldServer) {
            final EntityTracker tracker = ((WorldServer) world).getEntityTracker();
            for (EntityPlayer player : tracker.getTrackingPlayers(entity)) {
                Networking.CHANNEL.sendTo(msg, (EntityPlayerMP) player);
            }
        } else {
            setProp(FMLClientHandler.instance().getWorldClient().getEntityByID(msg.entityId), msg);
        }
    }

    public static void syncServer(Entity entity, SyncableExtProp prop) {
        final MessageSyncProp msg = new MessageSyncProp(entity, prop);
        Networking.CHANNEL.sendToServer(msg);
        if (Minecraft.getMinecraft().theWorld.isRemote) {
            prop.loadNBTData(msg.compound);
        }
    }

    @Override
    public IMessage onMessage(MessageSyncProp message, MessageContext ctx) {
        if (ctx.side.isClient()) {
            setProp(FMLClientHandler.instance().getWorldClient().getEntityByID(message.entityId), message);
            setProp(FMLClientHandler.instance().getServer().getEntityWorld().getEntityByID(message.entityId), message);
        } else {
            setProp(ctx.getServerHandler().playerEntity.worldObj.getEntityByID(message.entityId), message);
        }
        return null;
    }

    private static void setProp(Entity e, MessageSyncProp message) {
        if (e != null) {
            final IExtendedEntityProperties prop = e.getExtendedProperties(message.propName);
            if (prop != null)
                prop.loadNBTData(message.compound);
        }
    }
}
