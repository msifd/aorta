package msifeed.mc.aorta.attributes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public abstract class PlayerAttribute<T> extends EntityLivingAttribute<T> {
    public void onEntityConstruct(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer)
            event.entity.registerExtendedProperties(getName(), new AttrProp<>(this));
    }

    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.world.isRemote && event.entity instanceof EntityPlayer)
            broadcast(event.world, event.entity);
    }

    public void onPlayerStartedTracking(PlayerEvent.StartTracking event) {
        if (!event.target.worldObj.isRemote && event.target instanceof EntityPlayer)
            broadcast((EntityPlayerMP) event.entityPlayer, event.target);
    }
}
