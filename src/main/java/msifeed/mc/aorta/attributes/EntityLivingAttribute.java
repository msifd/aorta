package msifeed.mc.aorta.attributes;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public abstract class EntityLivingAttribute<T> extends EntityAttribute<T> {
    public void onEntityConstruct(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityLivingBase)
            event.entity.registerExtendedProperties(getName(), new AttrProp<>(this));
    }

    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.world.isRemote && event.entity instanceof EntityLivingBase)
            sync(event.world, event.entity);
    }

    public void onPlayerStartedTracking(PlayerEvent.StartTracking event) {
        if (!event.target.worldObj.isRemote && event.target instanceof EntityLivingBase)
            sync((EntityPlayerMP) event.entityPlayer, event.target);
    }
}
