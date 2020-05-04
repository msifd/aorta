package msifeed.mc.more.crabs.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Optional;

public class GetUtils {
    public static Optional<EntityLivingBase> entityLiving(Entity ew, int id) {
        final Entity entity = ew.worldObj.getEntityByID(id);
        if (entity instanceof EntityLivingBase)
            return Optional.of((EntityLivingBase) entity);
        else
            return Optional.empty();
    }

    public static Optional<EntityPlayer> entityPlayer(Entity ew, int id) {
        final Entity entity = ew.worldObj.getEntityByID(id);
        if (entity instanceof EntityPlayer)
            return Optional.of((EntityPlayer) entity);
        else
            return Optional.empty();
    }
}
