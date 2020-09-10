package msifeed.mc.more.crabs.combat;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.commons.traits.Trait;
import msifeed.mc.more.More;
import msifeed.mc.more.crabs.action.Action;
import msifeed.mc.more.crabs.action.ActionRegistry;
import msifeed.mc.more.crabs.action.ActionTag;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.action.effects.Effect;
import msifeed.mc.more.crabs.action.parser.EffectStringParser;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.meta.MetaInfo;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.more.crabs.utils.GetUtils;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import msifeed.mc.sys.rpc.RpcMethod;
import msifeed.mc.sys.rpc.RpcMethodException;
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

    private final static String addBuff = Bootstrap.MODID + ":combat.buff.add";
    private final static String removeBuff = Bootstrap.MODID + ":combat.buff.remove";

    // // // //

    public static void doAction(int entityId, String actionId) {
        More.RPC.sendToServer(doAction, entityId, actionId);
    }

    @RpcMethod(doAction)
    public void onDoAction(MessageContext ctx, int entityId, String actionId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .orElseThrow(() -> new RpcMethodException(sender, "invalid target entity"));

        final Action action = ActionRegistry.getFullAction(actionId);
        if (action == null || action.hasAnyTag(ActionTag.hidden))
            throw new RpcMethodException(sender, "unknown action: " + actionId);

        final CombatContext com = CombatAttribute.get(target)
                .orElseThrow(() -> new RpcMethodException(sender, "target is not a combatant"));

        if (!com.phase.isInCombat())
            throw new RpcMethodException(sender, "target is not in combat");

        CombatManager.INSTANCE.doAction(target, com, action);
    }

    public static void endAttack(int entityId) {
        More.RPC.sendToServer(endAttack, entityId);
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
        More.RPC.sendToServer(join, entityId, false);
    }
    public static void training(int entityId) {
        More.RPC.sendToServer(join, entityId, true);
    }

    @RpcMethod(join)
    public void onJoin(MessageContext ctx, int entityId, boolean training) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .orElseThrow(() -> new RpcMethodException(sender, "invalid target entity"));

        final CombatContext com = CombatAttribute.get(target).orElse(null);
        if (com == null)
            throw new RpcMethodException(sender, "target is not a combatant");
        if (com.phase.isInCombat())
            throw new RpcMethodException(sender, "target is already in combat");

        com.hardReset();
        com.healthBeforeJoin = training ? target.getHealth() : 0;
        CombatManager.INSTANCE.joinCombat(target, com);
    }

    public static void leave(int entityId) {
        More.RPC.sendToServer(leave, entityId);
    }

    @RpcMethod(leave)
    public void onLeave(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .orElseThrow(() -> new RpcMethodException(sender, "invalid target entity"));

        final CombatContext com = CombatAttribute.get(target).orElse(null);
        if (com == null)
            throw new RpcMethodException(sender, "target is not a combatant");
        if (!com.phase.isInCombat())
            throw new RpcMethodException(sender, "target is not in combat");
        if (com.phase != CombatContext.Phase.IDLE)
            throw new RpcMethodException(sender, "target is not in " + CombatContext.Phase.IDLE.toString() + " stage");

        CombatManager.INSTANCE.removeFromCombat(target, com);
    }

    public static void reset(int entityId) {
        More.RPC.sendToServer(reset, entityId);
    }

    @RpcMethod(reset)
    public void onReset(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .orElseThrow(() -> new RpcMethodException(sender, "invalid target entity"));

        final CombatContext com = CombatAttribute.get(target).orElse(null);
        if (com == null)
            throw new RpcMethodException(sender, "target is not a combatant");
        CombatManager.resetCombatantWithRelatives(target);
    }

    // // // //

    public static void addCombatToEntity(int entityId) {
        More.RPC.sendToServer(addCombat, entityId);
    }

    public static void removeCombatFromEntity(int entityId) {
        More.RPC.sendToServer(removeCombat, entityId);
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
        More.RPC.sendToServer(setPuppet, entityId);
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

    // // // //

    public static void addBuff(int entityId, Buff buff) {
        More.RPC.sendToServer(addBuff, entityId, buff.encode());
    }

    public static void removeBuff(int entityId, int index) {
        More.RPC.sendToServer(removeBuff, entityId, index);
    }

    @RpcMethod(addBuff)
    public void onAddBuff(MessageContext ctx, int entityId, String buffLine) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        if (!CharacterAttribute.has(sender, Trait.gm))
            throw new RpcMethodException(sender, "you are not GM");

        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .orElseThrow(() -> new RpcMethodException(sender, "invalid target entity"));

        final CombatContext com = CombatAttribute.get(target).orElse(null);
        if (com == null)
            throw new RpcMethodException(sender, "target is not a combatant");

        final Buff buff;
        try {
            final Effect effect = EffectStringParser.parseEffect(buffLine);
            if (effect instanceof Buff)
                buff = (Buff) effect;
            else
                throw new RpcMethodException(sender, "provided effect is not a buff");
        } catch (RpcMethodException e) {
            throw e;
        } catch (Exception e) {
            throw new RpcMethodException(sender, "can't decode buff");
        }

        com.buffs.add(buff);
        CombatAttribute.INSTANCE.set(target, com);
    }

    @RpcMethod(removeBuff)
    public void onRemoveBuff(MessageContext ctx, int entityId, int index) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        if (!CharacterAttribute.has(sender, Trait.gm))
            throw new RpcMethodException(sender, "you are not GM");

        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .orElseThrow(() -> new RpcMethodException(sender, "invalid target entity"));

        final CombatContext com = CombatAttribute.get(target).orElse(null);
        if (com == null)
            throw new RpcMethodException(sender, "target is not a combatant");

        if (index < 0 || index >= com.buffs.size())
            throw new RpcMethodException(sender, "invalid index");

        com.buffs.remove(index);
        CombatAttribute.INSTANCE.set(target, com);
    }
}
