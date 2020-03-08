package msifeed.mc.sys.attributes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public abstract class PlayerAttribute<T> extends EntityLivingAttribute<T> {
    @Override
    public void onEntityConstruct(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer)
            event.entity.registerExtendedProperties(getName(), new AttrProp<>(this));
    }

    @Override
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayer)
            broadcast(event.world, event.entity);
    }

    @Override
    public void onPlayerStartedTracking(PlayerEvent.StartTracking event) {
        if (event.target instanceof EntityPlayer)
            broadcast((EntityPlayerMP) event.entityPlayer, event.target);
    }
}
