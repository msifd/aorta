package msifeed.mc.more.crabs.combat;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import msifeed.mc.more.crabs.action.Action;
import msifeed.mc.more.crabs.action.ActionRegistry;
import msifeed.mc.more.crabs.action.ActionTag;
import msifeed.mc.more.crabs.action.Combo;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.action.effects.Effect;
import msifeed.mc.more.crabs.rolls.Criticalness;
import msifeed.mc.more.crabs.rolls.Dices;
import msifeed.mc.more.crabs.utils.*;
import msifeed.mc.sys.attributes.MissingRequiredAttributeException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.*;
import java.util.stream.Collectors;

public enum CombatManager {
    INSTANCE;

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    public boolean doAction(EntityLivingBase self, CombatContext ctx, Action action) {
        final boolean actionChanged = ctx.action == null || !ctx.action.id.equals(action.id);

        if (ctx.phase == CombatContext.Phase.IDLE) {
            if (!action.isOffencive())
                return false;
            if (actionChanged)
                updateAction(self, action);
            else if (action.requiresNoRoll())
                endAction(self, ctx);
            return true;
        } else if (ctx.phase == CombatContext.Phase.DEFEND) {
            if (ctx.targets.isEmpty())
                return false;
            final EntityLivingBase offender = GetUtils.entityLiving(self, ctx.targets.get(0)).orElse(null);
            if (offender == null)
                return false;
            final CombatContext offenderCom = CombatAttribute.require(offender);
            if (offenderCom.phase != CombatContext.Phase.WAIT)
                return false;
            final ActionContext offenderAct = ActionAttribute.require(offender);
            if (!action.isValidDefencive(offenderAct.action.getType()))
                return false;

            if (actionChanged)
                updateAction(self, action);
            else {
                ctx.phase = CombatContext.Phase.WAIT;
                if (!tryFinishMove(offender, offenderCom))
                    CombatAttribute.INSTANCE.set(self, ctx);
            }
            return true;
        }
        return false;
    }

    public void endAction(EntityLivingBase self, CombatContext com) {
        if (com.action.requiresNoRoll()) {
            finishSoloMove(new FighterInfo(self));
        } else {
            com.phase = CombatContext.Phase.WAIT;
            CombatAttribute.INSTANCE.set(self, com);
        }
    }

    private static void updateAction(EntityLivingBase self, Action action) {
        CombatAttribute.INSTANCE.update(self, com -> com.action = action);

        final ActionContext act = ActionAttribute.get(self).orElse(new ActionContext());
        act.updateAction(action);
        ActionAttribute.INSTANCE.set(self, act);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onHurtDamage(LivingHurtEvent event) {
        if (event.entityLiving.worldObj.isRemote)
            return;
        try {
            final Entity srcEntity = event.source.getEntity();
            if (srcEntity instanceof EntityLivingBase)
                handleEntityDamage(event, (EntityLivingBase) srcEntity);
            else
                handleNeutralDamage(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleNeutralDamage(LivingHurtEvent event) {
        final EntityLivingBase vicEntity = event.entityLiving;

        final CombatContext vicCom = CombatAttribute.get(vicEntity).orElse(null);
        if (vicCom == null)
            return;
        if (vicCom.phase == CombatContext.Phase.END)
            return; // Pass full damage
        if (vicCom.targets.size() != 1)
            return;

        final EntityLivingBase srcEntity;
        final CombatContext srcCom;
        try {
            final int targetId = vicCom.targets.get(0);
            final EntityLivingBase targetEntity = GetUtils.entityLiving(vicEntity, targetId).orElse(null);
            if (targetEntity == null)
                return;

            srcEntity = targetEntity;
            srcCom = CombatAttribute.require(targetEntity);
        } catch (MissingRequiredAttributeException e) {
            return;
        }

        event.setCanceled(true);

        if (srcEntity == vicEntity) // Do not attack your puppet
            return;
        if (cantDealDamage(srcCom, vicCom))
            return;

        addDealtDamage(vicEntity, new DamageAmount(event.source, event.ammount));
    }

    private void handleEntityDamage(LivingHurtEvent event, EntityLivingBase damageSrcEntity) {
        final EntityLivingBase vicEntity = event.entityLiving;

        final CombatContext vicCom = CombatAttribute.get(vicEntity).orElse(null);
        if (vicCom == null)
            return;

        if (vicCom.phase == CombatContext.Phase.END)
            return; // Pass full damage

        final EntityLivingBase srcEntity;
        final CombatContext srcCom;
        try {
            final CombatContext directCom = CombatAttribute.require(damageSrcEntity);
            if (directCom.puppet != 0) {
                final EntityLivingBase puppet = GetUtils.entityLiving(damageSrcEntity, directCom.puppet).orElse(null);
                if (puppet == null)
                    return;
                srcEntity = puppet;
                srcCom = CombatAttribute.require(puppet);
            } else {
                srcEntity = damageSrcEntity;
                srcCom = directCom;
            }
        } catch (MissingRequiredAttributeException e) {
            return;
        }

        event.setCanceled(true);

        if (srcEntity == vicEntity) // Do not attack your puppet
            return;
        if (cantDealDamage(srcCom, vicCom))
            return;

        boolean shouldUpdateOffender = false;
        if (srcCom.phase != CombatContext.Phase.ATTACK) {
            srcCom.role = CombatContext.Role.OFFENCE;
            srcCom.phase = CombatContext.Phase.ATTACK;
            srcCom.targets = new ArrayList<>();
            shouldUpdateOffender = true;
        }
        if (!srcCom.targets.contains(vicEntity.getEntityId())) {
            srcCom.targets.add(vicEntity.getEntityId());
            shouldUpdateOffender = true;
        }
        if (shouldUpdateOffender)
            CombatAttribute.INSTANCE.set(srcEntity, srcCom);

        if (vicCom.phase != CombatContext.Phase.DEFEND) {
            CombatAttribute.INSTANCE.update(vicEntity, context -> {
                context.role = CombatContext.Role.DEFENCE;
                context.phase = CombatContext.Phase.DEFEND;
                context.targets = Collections.singletonList(srcEntity.getEntityId());
            });
        }

        final float fistsMod;
        if (srcEntity.getHeldItem() == null)
            fistsMod = CharacterAttribute.get(srcEntity).map(c -> c.fistsDamage).orElse(0);
        else
            fistsMod = 0;
        final float modMod = MetaAttribute.get(srcEntity).map(m -> m.modifiers.damage).orElse(0);

        final float finalDamage = event.ammount + fistsMod + modMod;
        if (finalDamage > 0)
            addDealtDamage(vicEntity, new DamageAmount(event.source, finalDamage));
    }

    private boolean cantDealDamage(CombatContext srcCom, CombatContext vicCom) {
        if (srcCom.healthBeforeTraining > 0 != vicCom.healthBeforeTraining > 0)
            return true; // Ignore if someone is not in training
        if (srcCom.phase != CombatContext.Phase.IDLE && srcCom.phase != CombatContext.Phase.ATTACK)
            return true;
        if (srcCom.action == null)
            return true;
        if (vicCom.phase != CombatContext.Phase.IDLE && vicCom.phase != CombatContext.Phase.DEFEND)
            return true;

        return false;
    }

    private void addDealtDamage(EntityLivingBase vicEntity, DamageAmount amount) {

        final ActionContext act = ActionAttribute.get(vicEntity).orElseGet(() -> {
            final ActionContext a = new ActionContext();
            ActionAttribute.INSTANCE.set(vicEntity, a);
            return a;
        });

        act.damageDealt.add(amount);
    }

    private boolean tryFinishMove(EntityLivingBase offender, CombatContext offenderCom) {
        final List<EntityLivingBase> defenderEntities =  offenderCom.targets.stream()
                .map(id -> GetUtils.entityLiving(offender, id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        final List<CombatContext> defenderComs = defenderEntities.stream()
                .map(CombatAttribute::require)
                .collect(Collectors.toList());
        final boolean allDefendersAreWaiting = defenderComs.stream()
                .allMatch(ctx -> ctx.phase == CombatContext.Phase.WAIT);

        final boolean everyoneIsReady = defenderEntities.size() == defenderComs.size() && allDefendersAreWaiting;
        if (!everyoneIsReady)
            return false;

        finishMove(offender, defenderEntities);
        return true;
    }

    private void finishMove(EntityLivingBase offenderEntity, List<EntityLivingBase> defenderEntities) {
        final FighterInfo offender = new FighterInfo(offenderEntity);
        final List<FighterInfo> defenders = defenderEntities.stream().map(FighterInfo::new).collect(Collectors.toList());

        offender.com.phase = CombatContext.Phase.END;
        for (FighterInfo defender : defenders)
            defender.com.phase = CombatContext.Phase.END;

        applyScores(offender);
        for (FighterInfo defender : defenders) {
            do {
                applyScores(defender);
            } while (offender.act.compareTo(defender.act) == 0);
        }

        for (FighterInfo defender : defenders) {
            if (offender.act.compareTo(defender.act) > 0)
                applyEffectsAndResults(offender, defender);
            else
                applyEffectsAndResults(defender, offender);
        }

        cleanupMove(offender);
        for (FighterInfo defender : defenders)
            cleanupMove(defender);

        CombatAttribute.INSTANCE.set(offender.entity, offender.com);
        for (FighterInfo defender : defenders)
            CombatAttribute.INSTANCE.set(defender.entity, defender.com);
    }

    private static void applyScores(FighterInfo self) {
        if (self.act.action.requiresNoRoll())
            return;

        self.act.resetScore();

        self.act.scorePlayerMod = self.mod.roll;
        self.act.critical = Dices.critical();
        if (self.act.critical == Criticalness.FAIL)
            self.act.successful = false;

        applyBuffs(self.com.buffs, Effect.Stage.PRE_SCORE, self, null);
        applyEffects(self.act.action.self, Effect.Stage.SCORE, self, null);
        applyBuffs(self.com.buffs, Effect.Stage.SCORE, self, null);
    }

    private static void applyEffectsAndResults(FighterInfo winner, FighterInfo looser) {
        applyEffects(winner.act.action.self, Effect.Stage.ACTION, winner, looser);
        applyEffects(winner.act.action.target, Effect.Stage.ACTION, looser, winner);

        applyBuffs(winner.com.buffs, Effect.Stage.ACTION, winner, looser);
        applyBuffs(looser.com.buffs, Effect.Stage.ACTION, looser, winner);

        if (winner.act.action.isOffencive() && !winner.act.action.requiresNoRoll()) {
            final Combo.ComboLookup combo = Combo.find(ActionRegistry.getCombos(), winner.com.prevActions, winner.act.action.id);

            if (combo != null) {
                winner.com.prevActions.removeAll(combo.match);
                winner.comboAction = combo.c.action;
                applyEffects(combo.c.action.self, Effect.Stage.ACTION, winner, looser);
                applyEffects(combo.c.action.target, Effect.Stage.ACTION, looser, winner);
            } else {
                winner.com.addPrevAction(winner.act.action.id);
            }
        }

        applyActionResults(winner);
        applyActionResults(looser);

        CombatNotifications.actionResult(winner, looser);
    }

    private void finishSoloMove(FighterInfo self) {
        self.com.phase = CombatContext.Phase.END;

        if (self.act.action.hasAnyTag(ActionTag.apply)) {
            self.act.buffsToReceive.addAll(PotionsHandler.convertPotionEffects(self.entity));
        }

        applyEffects(self.act.action.self, Effect.Stage.ACTION, self, null);
        applyBuffs(self.com.buffs, Effect.Stage.ACTION, self, null);
        applyActionResults(self);

        CombatNotifications.soloMoveResult(self);

        cleanupMove(self);
        CombatAttribute.INSTANCE.set(self.entity, self.com);
    }

    private static void cleanupMove(FighterInfo self) {
        self.com.removeEndedEffects();
        resetCombatant(self.com);
        self.act.reset();
    }

    private static void applyBuffs(List<Buff> buffs, Effect.Stage stage, FighterInfo self, FighterInfo other) {
        for (Buff b : buffs)
            if (b.shouldApply(stage, self, other))
                b.applyEffect(stage, self, other);
    }

    private static void applyEffects(List<Effect> effects, Effect.Stage stage, FighterInfo self, FighterInfo other) {
        for (Effect e : effects)
            if (e.shouldApply(stage, self, other))
                e.apply(stage, self, other);
    }

    private static void applyActionResults(FighterInfo self) {
        final float healthBefore = self.entity.getHealth();
        for (DamageAmount da : self.act.damageToReceive) {
            self.entity.attackEntityFrom(da.source, da.amount);
            self.entity.hurtResistantTime = 0;
        }

        for (Buff buff : self.act.buffsToReceive)
            Buff.mergeBuff(self.com.buffs, buff);

        if (healthBefore - self.entity.getHealth() > 1)
            self.com.prevActions.clear();

        if (self.entity.isDead || self.entity.getHealth() <= 0) {
            if (self.com.knockedOut) {
                CombatNotifications.notifyKilled(self);
                if (self.com.healthBeforeTraining > 0) {
                    self.entity.setHealth(self.com.healthBeforeTraining);
                    self.entity.isDead = false;
                    self.com.knockedOut = false;
                }
            } else {
                self.entity.setHealth(1);
                self.entity.isDead = false;
                self.com.knockedOut = true;
                CombatNotifications.notifyKnockedOut(self);
            }
        }
    }

    @SubscribeEvent
    public void onLivingJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity.worldObj.isRemote || !(event.entity instanceof EntityLivingBase))
            return;

        CombatAttribute.get((EntityLivingBase) event.entity)
                .filter(com -> com.phase.isInCombat())
                .ifPresent(com -> resetCombatantWithRelatives((EntityLivingBase) event.entity));
    }

    @SubscribeEvent
    public void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player.worldObj.isRemote)
            return;

        final CombatContext com = CombatAttribute.require(event.player);
        if (com.phase.isInCombat())
            resetCombatantWithRelatives(event.player);
    }

    public void joinCombat(EntityLivingBase target, CombatContext com) {
        com.phase = CombatContext.Phase.IDLE;

        CombatAttribute.INSTANCE.set(target, com);
        ActionAttribute.INSTANCE.set(target, new ActionContext());
    }

    public void removeFromCombat(EntityLivingBase entity, CombatContext com) {
        resetCombatantWithRelatives(entity);

        if (com.healthBeforeTraining > 0)
            entity.setHealth(com.healthBeforeTraining);

        com.healthBeforeTraining = 0;
        com.phase = CombatContext.Phase.NONE;
        com.knockedOut = false;
        com.prevActions.clear();
        com.buffs.clear();
        ActionAttribute.remove(entity);
    }

    public static void resetCombatant(CombatContext com) {
        com.phase = com.phase.isInCombat() ? CombatContext.Phase.IDLE : CombatContext.Phase.NONE;
        com.role = CombatContext.Role.NONE;
        com.targets = Collections.emptyList();
        com.action = null;
    }

    public static void resetCombatantWithRelatives(EntityLivingBase entity) {
        final CombatContext com = CombatAttribute.get(entity).orElse(null);
        if (com == null)
            return;

        if (com.phase.isInCombat()) {
            if (com.role == CombatContext.Role.OFFENCE) {
                com.targets.stream()
                        .map(id -> GetUtils.entityLiving(entity, id))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(e -> CombatAttribute.get(e).ifPresent(c -> {
                            resetCombatant(c);
                            CombatAttribute.INSTANCE.set(e, c);
                        }));
            } else if (com.role == CombatContext.Role.DEFENCE) {
                com.targets.stream()
                        .map(id -> GetUtils.entityLiving(entity, id))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findFirst()
                        .ifPresent(e -> CombatAttribute.get(e).ifPresent(c -> {
                            c.targets.remove(Integer.valueOf(entity.getEntityId()));
                            if (c.targets.isEmpty())
                                resetCombatant(c);
                            CombatAttribute.INSTANCE.set(e, c);
                        }));
            }
            ActionAttribute.INSTANCE.set(entity, new ActionContext());
        } else {
            ActionAttribute.remove(entity);
        }

        resetCombatant(com);
        CombatAttribute.INSTANCE.set(entity, com);
    }
}
