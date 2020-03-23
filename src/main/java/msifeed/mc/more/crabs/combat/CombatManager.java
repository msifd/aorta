package msifeed.mc.more.crabs.combat;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import msifeed.mc.more.crabs.action.Action;
import msifeed.mc.more.crabs.action.ActionCritical;
import msifeed.mc.more.crabs.action.ActionTag;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.action.effects.Effect;
import msifeed.mc.more.crabs.action.effects.Score;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.rolls.Dices;
import msifeed.mc.more.crabs.rolls.Modifiers;
import msifeed.mc.more.crabs.utils.ActionAttribute;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import msifeed.mc.sys.attributes.MissingRequiredAttributeException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public enum CombatManager {
    INSTANCE;

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    public boolean doAction(EntityLivingBase self, CombatContext ctx, Action action) {
        final boolean actionChanged = ctx.action == null || !ctx.action.id.equals(action.id);

        if (ctx.phase == CombatContext.Phase.IDLE) {
            if (!ctx.acceptsOffendAction(action))
                return false;

            if (actionChanged)
                updateAction(self, action);
            else if (action.requiresNoRoll())
                finishSoloMove(new FighterInfo(self));

            return true;
        } else if (ctx.phase == CombatContext.Phase.DEFEND) {
            final Entity offender = self.worldObj.getEntityByID(ctx.target);
            if (offender == null)
                return false;
            final CombatContext offenderCom = CombatAttribute.require(offender);
            if (offenderCom.phase != CombatContext.Phase.WAIT)
                return false;
            final ActionContext offenderAct = ActionAttribute.require(offender);
            if (!ctx.acceptsDefendAction(offenderAct.action.getType(), action))
                return false;

            if (actionChanged)
                updateAction(self, action);
            else
                finishMove(new FighterInfo((EntityLivingBase) offender), new FighterInfo(self));

            return true;
        }

        return false;
    }

    private static void updateAction(EntityLivingBase self, Action action) {
        CombatAttribute.INSTANCE.update(self, context -> context.action = action);
        ActionAttribute.INSTANCE.set(self, new ActionContext(action));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHurtDamage(LivingHurtEvent event) {
        final Entity damageSrcEntity = event.source.getEntity();
        final EntityLivingBase vicEntity = event.entityLiving;

        if (!(damageSrcEntity instanceof EntityLivingBase) || vicEntity == null)
            return;

        if (vicEntity.worldObj.isRemote) {
//            System.out.println("client effect");
//            Minecraft.getMinecraft().effectRenderer.addEffect(new DamageParticle(vicEntity.worldObj, vicEntity.posX, vicEntity.posY, vicEntity.posZ, event.ammount));
            return;
        }

        if (((EntityLivingBase) damageSrcEntity).getHeldItem() == null) {
            CharacterAttribute.get(damageSrcEntity).ifPresent(character -> {
                if (character.fistsDamage > 0)
                    event.ammount = character.fistsDamage;
            });
        }

        final CombatContext vicCom;
        final EntityLivingBase srcEntity;
        final CombatContext srcCom;
        try {
            vicCom = CombatAttribute.require(vicEntity);

            final CombatContext directCom = CombatAttribute.require(damageSrcEntity);
            if (directCom.puppet != 0) {
                final Entity puppet = damageSrcEntity.worldObj.getEntityByID(directCom.puppet);
                if (!(puppet instanceof EntityLivingBase))
                    return;
                srcEntity = (EntityLivingBase) puppet;
                srcCom = CombatAttribute.require(puppet);
            } else {
                srcEntity = (EntityLivingBase) damageSrcEntity;
                srcCom = directCom;
            }
        } catch (MissingRequiredAttributeException e) {
            return;
        }

        if (vicCom.phase == CombatContext.Phase.END) // Pass real damage
            return;

        event.setCanceled(true);

        if (srcCom.phase != CombatContext.Phase.IDLE && srcCom.phase != CombatContext.Phase.ATTACK)
            return;
        if (srcEntity == vicEntity) // Do not attack your puppet
            return;
        if (srcCom.target != 0 && srcCom.target != vicEntity.getEntityId()) // Do not change first target
            return;
//        if (!ItemStack.areItemStackTagsEqual(srcEntity.getHeldItem(), srcCom.weapon))
//            return;

        final ActionContext srcAct = ActionAttribute.get(srcEntity).orElse(null);
        if (srcAct == null)
            return;

        if (srcCom.phase != CombatContext.Phase.ATTACK) {
            CombatAttribute.INSTANCE.update(srcEntity, context -> {
                context.phase = CombatContext.Phase.ATTACK;
                context.target = vicEntity.getEntityId();
            });
        }

        if (vicCom.phase != CombatContext.Phase.DEFEND) {
            CombatAttribute.INSTANCE.update(vicEntity, context -> {
                context.phase = CombatContext.Phase.DEFEND;
                context.target = srcEntity.getEntityId();
            });
        }

        srcAct.damageToDeal.add(new DamageAmount(event.source, event.ammount));

        // FIXME: implement attack window
        CombatAttribute.INSTANCE.update(srcEntity, context -> context.phase = CombatContext.Phase.WAIT);
    }

    private void finishMove(FighterInfo offender, FighterInfo defender) {
        offender.com.phase = CombatContext.Phase.END;
        defender.com.phase = CombatContext.Phase.END;

        while (offender.act.compareTo(defender.act) == 0) {
            applyScores(offender);
            applyScores(defender);
        }

        if (offender.act.compareTo(defender.act) > 0)
            applyEffectsAndResults(offender, defender);
        else
            applyEffectsAndResults(defender, offender);

        cleanupMove(offender);
        cleanupMove(defender);

        CombatAttribute.INSTANCE.set(offender.entity, offender.com);
        CombatAttribute.INSTANCE.set(defender.entity, defender.com);
    }

    private static void applyScores(FighterInfo self) {
        if (self.act.action.requiresNoRoll())
            return;

        self.act.resetScore();

        self.act.scorePlayerMod = self.mod.roll;
        self.act.critical = Dices.critical();
        if (self.act.critical == ActionCritical.FAIL)
            self.act.successful = false;

        for (Score s : self.act.action.score)
            self.act.scoreScores += s.mod(self.chr, self.mod);

        applyBuffs(self.com.buffs, Effect.Stage.SCORE, self.act);
        applyEffects(self.act.action.self, Effect.Stage.SCORE, self.act, null);
    }

    private static void applyEffectsAndResults(FighterInfo winner, FighterInfo looser) {
        applyBuffs(winner.com.buffs, Effect.Stage.ACTION, winner.act);
        applyBuffs(looser.com.buffs, Effect.Stage.ACTION, looser.act);

        applyEffects(winner.act.action.self, Effect.Stage.ACTION, winner.act, looser.act);
        applyEffects(winner.act.action.target, Effect.Stage.ACTION, looser.act, winner.act);

        applyActionResults(winner);
        applyActionResults(looser);

        if (!winner.act.action.requiresNoRoll())
            winner.com.prevActions.add(winner.act.action.id);

        CombatNotifications.moveResult(winner, looser);
    }

    private void finishSoloMove(FighterInfo self) {
        self.com.phase = CombatContext.Phase.END;

        applyBuffs(self.com.buffs, Effect.Stage.ACTION, self.act);
        applyEffects(self.act.action.self, Effect.Stage.ACTION, self.act, null);
        applyActionResults(self);

        if (self.act.action.hasTag(ActionTag.equip)) {
            self.com.weapon = self.entity.getHeldItem();
            self.com.armor = self.entity.getTotalArmorValue();
        }

        CombatNotifications.soloMoveResult(self);

        cleanupMove(self);
        CombatAttribute.INSTANCE.set(self.entity, self.com);
    }

    private static void cleanupMove(FighterInfo self) {
        self.com.removeEndedEffects();
        softReset(self.entity, self.com);
    }

    private static void applyBuffs(List<Buff> buffs, Effect.Stage stage, ActionContext self) {
        for (Buff b : buffs)
            if (b.shouldApply(stage, self, null))
                b.apply(self, null);
    }

    private static void applyEffects(List<Effect> effects, Effect.Stage stage, ActionContext self, ActionContext other) {
        for (Effect e : effects)
            if (e.shouldApply(stage, self, other))
                e.apply(self, other);
    }

    private static void applyActionResults(FighterInfo self) {
        boolean resetCombo = false;

        for (DamageAmount da : self.act.damageToReceive) {
            if (self.entity.attackEntityFrom(da.source, da.amount))
                resetCombo = true;
        }

        Buff.mergeBuffs(self.com.buffs, self.act.buffsToReceive);

        if (resetCombo && !self.com.prevActions.isEmpty()) {
            self.com.prevActions.clear();
            CombatNotifications.notify(self.entity, "combo breaker");
        }

        if (self.entity.isDead) {
            if (self.com.knockedOut) {
                CombatNotifications.notifyKilled(self.entity);
            } else {
                self.entity.setHealth(1);
                self.entity.isDead = false;
                CombatNotifications.notifyKnockedOut(self.entity);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.worldObj.getTotalWorldTime() % 20 != 0) return; //

        final CombatContext com = CombatAttribute.get(event.player).orElse(null);
        if (com == null) return;

        if (com.phase == CombatContext.Phase.LEAVE)
            removeFromCombat(event.player, com);
    }

    public static void removeFromCombat(EntityLivingBase entity, CombatContext com) {
        softReset(entity, com);
        com.phase = CombatContext.Phase.NONE;
        com.weapon = null;
        com.armor = 0;
    }

    public static void softReset(Entity entity, CombatContext com) {
        com.phase = com.phase.isInCombat() ? CombatContext.Phase.IDLE : CombatContext.Phase.NONE;
        com.target = 0;
        com.action = null;
        ActionAttribute.remove(entity);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent e) {
        CombatAttribute.get(e.entity).ifPresent(context -> validate(e.entity, context));
    }

    public static void validate(Entity entity, CombatContext com) {

    }

    public static void hardReset(Entity entity, CombatContext com) {
        softReset(entity, com);

        com.puppet = 0;

        com.knockedOut = false;
        com.buffs.clear();

        com.weapon = null;
        com.armor = 0;
        com.prevActions.clear();

        com.phase = CombatContext.Phase.NONE;
    }

    static class FighterInfo {
        final EntityLivingBase entity;
        final CombatContext com;
        final ActionContext act;
        final Character chr;
        final Modifiers mod;

        FighterInfo(EntityLivingBase entity) {
            this.entity = entity;
            this.com = CombatAttribute.require(entity);
            this.act = ActionAttribute.require(entity);
            this.chr = CharacterAttribute.require(entity);
            this.mod = MetaAttribute.require(entity).modifiers;
        }
    }
}
