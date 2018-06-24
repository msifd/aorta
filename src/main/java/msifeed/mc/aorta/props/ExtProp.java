package msifeed.mc.aorta.props;

import msifeed.mc.aorta.network.Networking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IExtendedEntityProperties;

public abstract class ExtProp implements IExtendedEntityProperties {

    public abstract String getName();

    public void sync(EntityPlayer playerMP, Entity entity) {
        final SyncPropMessage msg = new SyncPropMessage(entity, this);
        Networking.CHANNEL.sendTo(msg, (EntityPlayerMP) playerMP);
    }

    public void sync(World world, Entity entity) {
        if (!(world instanceof WorldServer))
            return;

        final SyncPropMessage msg = new SyncPropMessage(entity, this);
        final EntityTracker tracker = ((WorldServer) world).getEntityTracker();
        for (EntityPlayer player : tracker.getTrackingPlayers(entity)) {
            Networking.CHANNEL.sendTo(msg, (EntityPlayerMP) player);
        }
        if (entity instanceof EntityPlayerMP) {
            Networking.CHANNEL.sendTo(msg, (EntityPlayerMP) entity);
        }
    }

    public void syncServer(Entity e) {
        final SyncPropMessage msg = new SyncPropMessage(e, this);
        Networking.CHANNEL.sendToServer(msg);
    }
}
