package msifeed.mc.more.crabs.combat;

import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.more.crabs.utils.GetUtils;
import net.minecraft.entity.EntityLivingBase;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CombatUtils {
    public static Set<EntityLivingBase> relativeEntites(EntityLivingBase entity, CombatContext com) {
        final EntityLivingBase offEntity;
        final CombatContext offCom;
        if (com.role == CombatContext.Role.DEFENCE) {
            offEntity = GetUtils.entityLiving(entity, com.offender).orElse(entity);
            offCom = CombatAttribute.get(offEntity).orElse(com);
        } else {
            offEntity = entity;
            offCom = com;
        }

        final Set<EntityLivingBase> rel = new HashSet<>();
        rel.add(offEntity);
        offCom.defenders.stream()
                .map(id -> GetUtils.entityLiving(entity, id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(rel::add);
        return rel;
    }
}
