package msifeed.mc.more.crabs.combat;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.more.crabs.action.Action;
import msifeed.mc.more.crabs.action.ActionRegistry;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.meta.MetaInfo;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.more.crabs.utils.GetUtils;
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
        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .orElseThrow(() -> new RpcMethodException(sender, "invalid target entity"));

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
        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .orElseThrow(() -> new RpcMethodException(sender, "invalid target entity"));

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
        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .orElseThrow(() -> new RpcMethodException(sender, "invalid target entity"));

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
        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .orElseThrow(() -> new RpcMethodException(sender, "invalid target entity"));

        CombatAttribute.INSTANCE.update(target, context -> {
            if (!context.phase.isInCombat())
                throw new RpcMethodException(sender, "target is not in combat");
            if (context.phase != CombatContext.Phase.IDLE)
                throw new RpcMethodException(sender, "target is not in " + CombatContext.Phase.IDLE.toString() + " stage");

            CombatManager.INSTANCE.removeFromCombat(target, context);
        });
    }

    public static void reset(int entityId) {
        Rpc.sendToServer(reset, entityId);
    }

    @RpcMethod(reset)
    public void onReset(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .orElseThrow(() -> new RpcMethodException(sender, "invalid target entity"));

        CombatManager.resetCombatantWithRelatives(target);
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
        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .filter(e -> !(e instanceof EntityPlayer))
                .orElseThrow(() -> new RpcMethodException(sender, "invalid target entity"));

        CharacterAttribute.INSTANCE.set(target, new Character());
        MetaAttribute.INSTANCE.set(target, new MetaInfo());
        CombatAttribute.INSTANCE.set(target, new CombatContext());
    }

    @RpcMethod(removeCombat)
    public void onRemoveCombatFromEntity(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .filter(e -> !(e instanceof EntityPlayer))
                .orElseThrow(() -> new RpcMethodException(sender, "invalid target entity"));

        CharacterAttribute.INSTANCE.set(target, null);
        MetaAttribute.INSTANCE.set(target, null);
        CombatAttribute.INSTANCE.set(target, null);
    }

    // // // //

    public static void setPuppet(int entityId) {
        Rpc.sendToServer(setPuppet, entityId);
    }

    @RpcMethod(setPuppet)
    public void onSetPuppet(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;

        if (sender.getEntityId() == entityId)
            throw new RpcMethodException(sender, "you can't puppet yourself, silly");

        if (entityId != 0) {
            GetUtils.entityLiving(sender, entityId)
                    .orElseThrow(() -> new RpcMethodException(sender, "puppet target is not found"));
        }

        CombatAttribute.INSTANCE.update(sender, context -> context.puppet = entityId);
    }
}
