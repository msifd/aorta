package msifeed.mc.more.crabs.utils;

import msifeed.mc.Bootstrap;
import msifeed.mc.more.crabs.combat.ActionContext;
import msifeed.mc.sys.attributes.EntityLivingAttribute;
import msifeed.mc.sys.attributes.MissingRequiredAttributeException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Optional;

public final class ActionAttribute extends EntityLivingAttribute<ActionContext> {
    public static final ActionAttribute INSTANCE = new ActionAttribute();
    private static final String PROP_NAME = Bootstrap.MODID + ".crabs.act";

    public static Optional<ActionContext> get(EntityLivingBase e) {
        return INSTANCE.getValue(e);
    }

    public static ActionContext require(EntityLivingBase e) {
        return INSTANCE.getValue(e).orElseThrow(() -> new MissingRequiredAttributeException(INSTANCE, e));
    }

    public static void remove(EntityLivingBase e) {
        INSTANCE.set(e, null);
    }

    private ActionAttribute() {
    }

    @Override
    public String getName() {
        return PROP_NAME;
    }

    @Override
    public ActionContext init(Entity entity, World world, ActionContext context) {
        return null;
//        if (context != null)
//            return context;
//        else if (entity instanceof EntityPlayer)
//            return new CombatContext();
//        else
//            return null;
    }

    @Override
    public void saveNBTData(ActionContext context, NBTTagCompound root) {
//        if (context == null)
//            return;
//        root.setTag(PROP_NAME, context.toNBT());
    }

    @Override
    public ActionContext loadNBTData(ActionContext value, NBTTagCompound root) {
        return null;

//        if (!root.hasKey(PROP_NAME))
//            return null;
//
//        final CombatContext context = new CombatContext();
//        context.fromNBT(root.getCompoundTag(PROP_NAME));
//        return context;
    }

    @Override
    public boolean shouldSync(ActionContext value) {
        return false;
    }
}
