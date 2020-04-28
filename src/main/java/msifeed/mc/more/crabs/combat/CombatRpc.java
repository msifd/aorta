package msifeed.mc.more.crabs.combat;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.more.crabs.action.Action;
import msifeed.mc.more.crabs.action.ActionRegistry;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.meta.MetaInfo;
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
    private final static String endAttack = Bootstrap.MODID + ":combat.endAttack";

    private final static String join = Bootstrap.MODID + ":combat.join";
    private final static String leave = Bootstrap.MODID + ":combat.leave";
    private final static String reset = Bootstrap.MODID + ":combat.reset";

    private final static String addCombat = Bootstrap.MODID + ":combat.add";
    private final static String removeCombat = Bootstrap.MODID + ":combat.remove";

    private final static String setPuppet = Bootstrap.MODID + ":combat.set.puppet";

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

        final Action action = ActionRegistry.getFullAction(actionId);
        if (action == null)
            throw new RpcMethodException(sender, "unknown action: " + actionId);

        final CombatContext com = CombatAttribute.get(target)
                .orElseThrow(() -> new RpcMethodException(sender, "target is not a combatant"));

        if (!com.phase.isInCombat())
            throw new RpcMethodException(sender, "target is not in combat");

        CombatManager.INSTANCE.doAction(target, com, action);
    }

    public static void endAttack(int entityId) {
        Rpc.sendToServer(endAttack, entityId);
    }

    @RpcMethod(endAttack)
    public void onEndAttack(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity targetEntity = sender.worldObj.getEntityByID(entityId);

        if (!(targetEntity instanceof EntityLivingBase))
            throw new RpcMethodException(sender, "target is not a living entity");
        final EntityLivingBase target = (EntityLivingBase) targetEntity;

        final CombatContext com = CombatAttribute.get(target)
                .orElseThrow(() -> new RpcMethodException(sender, "target is not a combatant"));

        if (com.phase != CombatContext.Phase.ATTACK)
            throw new RpcMethodException(sender, "target is attacking!");

        CombatManager.INSTANCE.endAction(target, com);
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
        final CombatContext com = CombatAttribute.get(target).orElse(null);
        if (com == null)
            throw new RpcMethodException(sender, "target is not a combatant");
        if (com.phase.isInCombat())
            throw new RpcMethodException(sender, "target is already in combat");

        CombatManager.INSTANCE.joinCombat(target, com);
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
            if (!context.phase.isInCombat())
                throw new RpcMethodException(sender, "target is not in combat");
            if (context.phase != CombatContext.Phase.IDLE)
                throw new RpcMethodException(sender, "target is not in " + CombatContext.Phase.IDLE.toString() + " stage");

            CombatManager.INSTANCE.removeFromCombat(targetEntity, context);
//            if (targetEntity instanceof EntityPlayer)
//                context.phase = CombatContext.Phase.LEAVE;
//            else
//                CombatManager.INSTANCE.removeFromCombat((EntityLivingBase) targetEntity, context);
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

        CombatAttribute.INSTANCE.update(targetEntity, CombatManager::softReset);
    }

    // // // //

    public static void addCombatToEntity(int entityId) {
        Rpc.sendToServer(addCombat, entityId);
    }

    public static void removeCombatFromEntity(int entityId) {
        Rpc.sendToServer(removeCombat, entityId);
    }

    @RpcMethod(addCombat)
    public void onAddCombatToEntity(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity targetEntity = sender.worldObj.getEntityByID(entityId);

        if (!(targetEntity instanceof EntityLivingBase) || targetEntity instanceof EntityPlayer)
            throw new RpcMethodException(sender, "target is not a living entity");

        CharacterAttribute.INSTANCE.set(targetEntity, new Character());
        MetaAttribute.INSTANCE.set(targetEntity, new MetaInfo());
        CombatAttribute.INSTANCE.set(targetEntity, new CombatContext());
    }

    @RpcMethod(removeCombat)
    public void onRemoveCombatFromEntity(MessageContext ctx, int entityId) {
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
}
