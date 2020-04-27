package msifeed.mc.more.crabs.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.util.Optional;

public class GetUtils {
    public static Optional<EntityLivingBase> entityLiving(Entity ew, int id) {
        final Entity entity = ew.worldObj.getEntityByID(id);
        if (entity instanceof EntityLivingBase)
            return Optional.of((EntityLivingBase) entity);
        else
            return Optional.empty();
    }
}
