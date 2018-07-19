package msifeed.mc.aorta.core.props;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PropsHandler {
    @SubscribeEvent
    public void onEntityConstruct(EntityEvent.EntityConstructing e) {
        if (e.entity instanceof EntityLivingBase) {
            e.entity.registerExtendedProperties(CharacterProperty.PROP_NAME, new CharacterProperty());
            e.entity.registerExtendedProperties(TraitsProperty.PROP_NAME, new TraitsProperty());
        }
    }

    @SubscribeEvent
    public void entityJoinWorld(EntityJoinWorldEvent e) {
        if (!e.world.isRemote && e.entity instanceof EntityLivingBase) {
            final CharacterProperty cProp = CharacterProperty.get((EntityLivingBase) e.entity);
            final TraitsProperty tProp = TraitsProperty.get((EntityLivingBase) e.entity);
            cProp.sync(e.world, e.entity);
            tProp.sync(e.world, e.entity);
        }
    }

    @SubscribeEvent
    public void playerStartedTracking(PlayerEvent.StartTracking e) {
        if (!e.target.worldObj.isRemote && e.target instanceof EntityLivingBase) {
            final CharacterProperty cProp = CharacterProperty.get((EntityLivingBase) e.target);
            final TraitsProperty tProp = TraitsProperty.get((EntityLivingBase) e.target);
            cProp.sync(e.entityPlayer, e.target);
            tProp.sync(e.entityPlayer, e.target);
        }
    }
}
