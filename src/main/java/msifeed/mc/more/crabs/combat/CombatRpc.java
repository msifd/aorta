package msifeed.mc.more.crabs.combat;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.more.crabs.action.Action;
import msifeed.mc.more.crabs.action.ActionRegistry;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.meta.MetaInfo;
import msifeed.mc.more.crabs.utils.ActionAttribute;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import msifeed.mc.sys.rpc.Rpc;
import msifeed.mc.sys.rpc.RpcMethod;
import msifeed.mc.sys.rpc.RpcMethodException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public enum CombatRpc {
    INSTANCE;

    private final static String doAction = Bootstrap.MODID + ":combat.action.do";
    private final static String cancelAction = Bootstrap.MODID + ":combat.action.cancel";

    private final static String join = Bootstrap.MODID + ":combat.join";
    private final static String leave = Bootstrap.MODID + ":combat.leave";
    private final static String reset = Bootstrap.MODID + ":combat.reset";

    private final static String recruitEntity = Bootstrap.MODID + ":combat.recruit";
    private final static String dismissEntity = Bootstrap.MODID + ":combat.dismiss";

    private final static String setPuppet = Bootstrap.MODID + ":combat.set.puppet";
    private final static String setWeapon = Bootstrap.MODID + ":combat.set.weapon";
    private final static String setArmor = Bootstrap.MODID + ":combat.set.armor";

    // // // //

    public static void doAction(int entityId, String actionId) {
        Rpc.sendToServer(doAction, entityId, actionId);
    }

    @RpcMethod(doAction)
    public void onDoAction(MessageContext ctx, int entityId, String actionId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity targetEntity = sender.worldObj.getEntityByID(entityId);

        if (!(targetEntity instanceof EntityLivingBase))
            throw new RpcMethodException(sender, "target is not a living entity");
        final EntityLivingBase target = (EntityLivingBase) targetEntity;

        final Action action = ActionRegistry.get(actionId);
        if (action == null)
            throw new RpcMethodException(sender, "unknown action: " + actionId);

        CombatAttribute.INSTANCE.update(target, context -> {
            if (!context.stage.isInCombat())
                throw new RpcMethodException(sender, "target is not in combat");
            CombatManager.INSTANCE.doAction(target, context, action);
        });
    }

    public static void cancelAction(int entityId) {
        Rpc.sendToServer(cancelAction, entityId);
    }

    @RpcMethod(cancelAction)
    public void onCancelAction(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity targetEntity = sender.worldObj.getEntityByID(entityId);

        if (!(targetEntity instanceof EntityLivingBase))
            throw new RpcMethodException(sender, "target is not a living entity");
        final EntityLivingBase target = (EntityLivingBase) targetEntity;

        CombatAttribute.INSTANCE.update(target, context -> {
            if (!context.stage.isInCombat())
                throw new RpcMethodException(sender, "target is not in combat");
            if (context.stage != CombatContext.Stage.ACTION)
                throw new RpcMethodException(sender, "cannot reset target's action on that move stage");
            if (context.target != 0)
                throw new RpcMethodException(sender, "cannot reset target's action after its target selected");
            context.stage = CombatContext.Stage.IDLE;
            ActionAttribute.remove(target);
        });
    }

    // // // //

    public static void join(int entityId) {
        Rpc.sendToServer(join, entityId);
    }

    @RpcMethod(join)
    public void onJoin(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity targetEntity = sender.worldObj.getEntityByID(entityId);

        if (!(targetEntity instanceof EntityLivingBase))
            throw new RpcMethodException(sender, "target is not a living entity");
        final EntityLivingBase target = (EntityLivingBase) targetEntity;

        CombatAttribute.INSTANCE.update(target, context -> {
            if (context.stage.isInCombat())
                throw new RpcMethodException(sender, "target is already in combat");

            context.stage = CombatContext.Stage.IDLE;
            context.weapon = target.getHeldItem();
            context.armor = target.getTotalArmorValue();
        });
    }

    public static void leave(int entityId) {
        Rpc.sendToServer(leave, entityId);
    }

    @RpcMethod(leave)
    public void onLeave(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity targetEntity = sender.worldObj.getEntityByID(entityId);

        if (!(targetEntity instanceof EntityLivingBase))
            throw new RpcMethodException(sender, "target is not a living entity");

        CombatAttribute.INSTANCE.update(targetEntity, context -> {
            if (!context.stage.isInCombat())
                throw new RpcMethodException(sender, "target is not in combat");
            if (context.stage != CombatContext.Stage.IDLE)
                throw new RpcMethodException(sender, "target is not in " + CombatContext.Stage.IDLE.toString() + " stage");

            if (targetEntity instanceof EntityPlayer)
                context.stage = CombatContext.Stage.LEAVE;
            else
                CombatManager.removeFromCombat((EntityLivingBase) targetEntity, context);
        });
    }

    public static void reset(int entityId) {
        Rpc.sendToServer(reset, entityId);
    }

    @RpcMethod(reset)
    public void onReset(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity targetEntity = sender.worldObj.getEntityByID(entityId);

        if (!(targetEntity instanceof EntityLivingBase))
            throw new RpcMethodException(sender, "target is not a living entity");

        CombatAttribute.INSTANCE.update(targetEntity, context -> CombatManager.softReset(targetEntity, context));
    }

    // // // //

    public static void recruitEntity(int entityId) {
        Rpc.sendToServer(recruitEntity, entityId);
    }

    public static void dismissEntity(int entityId) {
        Rpc.sendToServer(dismissEntity, entityId);
    }

    @RpcMethod(recruitEntity)
    public void onRecruitEntity(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity targetEntity = sender.worldObj.getEntityByID(entityId);

        if (!(targetEntity instanceof EntityLivingBase) || targetEntity instanceof EntityPlayer)
            throw new RpcMethodException(sender, "target is not a living entity");

        CharacterAttribute.INSTANCE.set(targetEntity, new Character());
        MetaAttribute.INSTANCE.set(targetEntity, new MetaInfo());
        CombatAttribute.INSTANCE.set(targetEntity, new CombatContext());
    }

    @RpcMethod(dismissEntity)
    public void onDismissEntity(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity targetEntity = sender.worldObj.getEntityByID(entityId);

        if (!(targetEntity instanceof EntityLivingBase) || targetEntity instanceof EntityPlayer)
            throw new RpcMethodException(sender, "target is not a living entity");

        CharacterAttribute.INSTANCE.set(targetEntity, null);
        MetaAttribute.INSTANCE.set(targetEntity, null);
        CombatAttribute.INSTANCE.set(targetEntity, null);
    }

    // // // //

    public static void setPuppet(int entityId) {
        Rpc.sendToServer(setPuppet, entityId);
    }

    public static void setWeapon(int entityId) {
        Rpc.sendToServer(setWeapon, entityId);
    }

    public static void setArmor(int entityId) {
        Rpc.sendToServer(setArmor, entityId);
    }

    @RpcMethod(setPuppet)
    public void onSetPuppet(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;

        if (entityId != 0) {
            final Entity targetEntity = sender.worldObj.getEntityByID(entityId);
            if (!(targetEntity instanceof EntityLivingBase))
                throw new RpcMethodException(sender, "target not found");
            if (targetEntity == sender)
                throw new RpcMethodException(sender, "you can't puppet yourself, silly");
        }

        CombatAttribute.INSTANCE.update(sender, context -> context.puppet = entityId);
    }

    @RpcMethod(setWeapon)
    public void onSetWeapon(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity targetEntity = sender.worldObj.getEntityByID(entityId);

        if (!(targetEntity instanceof EntityLivingBase))
            throw new RpcMethodException(sender, "target not found");

        CombatAttribute.INSTANCE.update(targetEntity, context -> context.weapon = sender.getHeldItem());
    }

    @RpcMethod(setArmor)
    public void onSetArmor(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity targetEntity = sender.worldObj.getEntityByID(entityId);

        if (!(targetEntity instanceof EntityLivingBase))
            throw new RpcMethodException(sender, "target not found");

        CombatAttribute.INSTANCE.update(targetEntity, context -> context.armor = sender.getTotalArmorValue());
    }
}
