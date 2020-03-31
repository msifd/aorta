package msifeed.mc.more.crabs.utils;

import msifeed.mc.Bootstrap;
import msifeed.mc.more.crabs.combat.CombatContext;
import msifeed.mc.sys.attributes.EntityLivingAttribute;
import msifeed.mc.sys.attributes.MissingRequiredAttributeException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Optional;

public final class CombatAttribute extends EntityLivingAttribute<CombatContext> {
    public static final CombatAttribute INSTANCE = new CombatAttribute();
    private static final String PROP_NAME = Bootstrap.MODID + ".crabs.ctx";

    public static Optional<CombatContext> get(Entity e) {
        return INSTANCE.getValue(e);
    }

    public static CombatContext require(Entity e) {
        return INSTANCE.getValue(e).orElseThrow(() -> new MissingRequiredAttributeException(INSTANCE, e));
    }

    private CombatAttribute() {
    }

    @Override
    public String getName() {
        return PROP_NAME;
    }

    @Override
    public CombatContext init(Entity entity, World world, CombatContext context) {
        if (context != null)
            return context;
        else if (entity instanceof EntityPlayer)
            return new CombatContext();
        else
            return null;
    }

    @Override
    public void saveNBTData(CombatContext context, NBTTagCompound root) {
        if (context == null)
            return;
        root.setTag(PROP_NAME, context.toNBT());
    }

    @Override
    public CombatContext loadNBTData(CombatContext value, NBTTagCompound root) {
        if (!root.hasKey(PROP_NAME))
            return null;

        final CombatContext context = new CombatContext();
        context.fromNBT(root.getCompoundTag(PROP_NAME));
        return context;
    }
}
