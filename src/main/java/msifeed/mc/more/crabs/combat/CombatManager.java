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
import msifeed.mc.more.crabs.utils.ActionAttribute;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.more.crabs.utils.GetUtils;
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
                CombatAttribute.INSTANCE.update(self, com -> com.phase = CombatContext.Phase.WAIT);
                tryFinishMove(offender, offenderCom);
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
        if (srcCom.action == null)
            return;
        if (vicCom.phase != CombatContext.Phase.IDLE && vicCom.phase != CombatContext.Phase.DEFEND)
            return;
        if (srcEntity == vicEntity) // Do not attack your puppet
            return;
//        if (!ItemStack.areItemStackTagsEqual(srcEntity.getHeldItem(), srcCom.weapon))
//            return;

        final ActionContext vicAct = ActionAttribute.require(vicEntity);

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

        vicAct.damageDealt.add(new DamageAmount(event.source, event.ammount));
    }

    private void tryFinishMove(EntityLivingBase offender, CombatContext offenderCom) {
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
        if (everyoneIsReady)
            finishMove(offender, defenderEntities);
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

        applyEffects(self.act.action.self, Effect.Stage.ACTION, self, null);
        applyBuffs(self.com.buffs, Effect.Stage.ACTION, self, null);
        applyActionResults(self);

        if (self.act.action.hasAnyTag(ActionTag.apply)) {
            PotionsHandler.convertPotionEffects(self.entity, self.com);
        }

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
                b.applyEffect(self, other);
    }

    private static void applyEffects(List<Effect> effects, Effect.Stage stage, FighterInfo self, FighterInfo other) {
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

        for (Buff buff : self.act.buffsToReceive)
            Buff.mergeBuff(self.com.buffs, buff);

        if (resetCombo && !self.com.prevActions.isEmpty())
            self.com.prevActions.clear();

        if (self.entity.isDead || self.entity.getHealth() <= 0) {
            if (self.com.knockedOut) {
                CombatNotifications.notifyKilled(self.entity);
            } else {
                self.entity.setHealth(1);
                self.entity.isDead = false;
                self.com.knockedOut = true;
                CombatNotifications.notifyKnockedOut(self.entity);
            }
        }
    }

//    @SubscribeEvent
//    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        if (event.phase != TickEvent.Phase.END) return;
//        if (event.player.worldObj.getTotalWorldTime() % 20 != 0) return;
//
//        final CombatContext com = CombatAttribute.get(event.player).orElse(null);
//        if (com == null) return;
//
//        if (com.phase == CombatContext.Phase.LEAVE)
//            removeFromCombat(event.player, com);
//    }

    @SubscribeEvent
    public void onLivingJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity.worldObj.isRemote || !(event.entity instanceof EntityLivingBase))
            return;

        final CombatContext com = CombatAttribute.get(event.entity).orElse(null);
        if (com != null && com.phase.isInCombat())
            resetCombatantWithRelatives(event.entity);
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

    public void removeFromCombat(Entity entity, CombatContext com) {
        resetCombatantWithRelatives(entity);

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

    public static void resetCombatantWithRelatives(Entity entity) {
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
