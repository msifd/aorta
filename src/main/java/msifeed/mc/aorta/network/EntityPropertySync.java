package msifeed.mc.aorta.network;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IExtendedEntityProperties;

public class EntityPropertySync implements IMessage {
    private int entityId;
    private String propName;
    private NBTTagCompound compound;

    public EntityPropertySync() {
    }

    private EntityPropertySync(Entity entity, ISyncProp prop) {
        this.entityId = entity.getEntityId();
        this.propName = prop.getName();
        this.compound = new NBTTagCompound();
        prop.saveNBTData(this.compound);
    }

    public static void sync(EntityPlayerMP playerMP, Entity entity, ISyncProp prop) {
        final EntityPropertySync msg = new EntityPropertySync();
        msg.entityId = entity.getEntityId();
        msg.propName = prop.getName();
        msg.compound = new NBTTagCompound();
        prop.saveNBTData(msg.compound);

        Networking.CHANNEL.sendTo(msg, playerMP);
    }

    public static void sync(World world, Entity entity, ISyncProp prop) {
        if (!world.isRemote || !(world instanceof WorldServer))
            return;

        final EntityPropertySync msg = new EntityPropertySync(entity, prop);
        final EntityTracker tracker = ((WorldServer) world).getEntityTracker();
        for (EntityPlayer player : tracker.getTrackingPlayers(entity)) {
            Networking.CHANNEL.sendTo(msg, (EntityPlayerMP) player);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        propName = ByteBufUtils.readUTF8String(buf);
        compound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        ByteBufUtils.writeUTF8String(buf, propName);
        ByteBufUtils.writeTag(buf, compound);
    }

    public interface ISyncProp extends IExtendedEntityProperties {
        String getName();
    }

    public static class Handler implements IMessageHandler<EntityPropertySync, IMessage> {
        @Override
        public IMessage onMessage(EntityPropertySync message, MessageContext ctx) {
            final Entity e = FMLClientHandler.instance().getWorldClient().getEntityByID(message.entityId);
            if (e != null) {
                final IExtendedEntityProperties prop = e.getExtendedProperties(message.propName);
                if (prop != null)
                    prop.loadNBTData(message.compound);
            }
            return null;
        }
    }
}
