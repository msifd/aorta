package msifeed.mc.more.crabs.combat;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import msifeed.mc.more.client.combat.other.DamageParticle;
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
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public enum CombatManager {
    INSTANCE;

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    public void doAction(EntityLivingBase self, CombatContext ctx, Action action) {
        if (!ctx.stage.isInCombat() || ctx.discardAction(action))
            return;

        if (ctx.stage == CombatContext.Stage.IDLE) {
            // Attack
            ActionAttribute.create(self, ctx, action);
            ctx.action = action;

            if (action.isPassive())
                finishSoloMove(new FighterInfo(self));
            else
                ctx.stage = CombatContext.Stage.ACTION;
        } else {
            // Defence
            final Entity offender = self.worldObj.getEntityByID(ctx.target);
            if (!(offender instanceof EntityLivingBase)) {
                softReset(self, ctx);
                return;
            }

            if (ActionAttribute.require(offender).action.getType() != action.getType())
                return;

            ActionAttribute.create(self, ctx, action);
            ctx.action = action;
            finishMove(new FighterInfo((EntityLivingBase) offender), new FighterInfo(self));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHurtDamage(LivingHurtEvent event) {
        final Entity damageSrcEntity = event.source.getEntity();
        final EntityLivingBase vicEntity = event.entityLiving;

        if (!(damageSrcEntity instanceof EntityLivingBase) || vicEntity == null)
            return;

        if (vicEntity.worldObj.isRemote) {
            System.out.println("client effect");
            Minecraft.getMinecraft().effectRenderer.addEffect(new DamageParticle(vicEntity.worldObj, vicEntity.posX, vicEntity.posY, vicEntity.posZ, event.ammount));
            return;
        }

        final EntityLivingBase srcEntity;
        final CombatContext srcCom;
        final CombatContext vicCom;
        try {
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
            vicCom = CombatAttribute.require(vicEntity);
        } catch (MissingRequiredAttributeException e) {
            return;
        }

        if (vicCom.stage == CombatContext.Stage.END) // Pass real damage
            return;

        event.setCanceled(true);

        final ActionContext srcAct = ActionAttribute.get(srcEntity).orElse(null);
        if (srcCom.stage != CombatContext.Stage.ACTION || srcAct == null)
            return;
        if (srcEntity == vicEntity) // Do not attack your puppet
            return;
        if (srcCom.target != 0 && srcCom.target != vicEntity.getEntityId()) // Do not change first target
            return;
        if (!ItemStack.areItemStackTagsEqual(srcEntity.getHeldItem(), srcCom.weapon))
            return;

        final boolean attack = srcCom.target == 0;

        srcCom.stage = CombatContext.Stage.WAIT;
        srcAct.damageToDeal.add(new DamageAmount(event.source, event.ammount));

        if (attack) {
            srcCom.target = vicEntity.getEntityId();

            vicCom.stage = CombatContext.Stage.DEFEND;
            vicCom.target = srcEntity.getEntityId();

            CombatAttribute.INSTANCE.set(srcEntity, srcCom);
            CombatAttribute.INSTANCE.set(vicEntity, vicCom);
        } else {
            finishMove(new FighterInfo(vicEntity), new FighterInfo(srcEntity));
        }
    }

    private void finishMove(FighterInfo attacker, FighterInfo defender) {
        attacker.com.stage = CombatContext.Stage.END;
        defender.com.stage = CombatContext.Stage.END;

        while (attacker.act.compareTo(defender.act) == 0) {
            applyScores(attacker);
            applyScores(defender);
        }

        if (attacker.act.compareTo(defender.act) > 0)
            applyEffectsAndResults(attacker, defender);
        else
            applyEffectsAndResults(defender, attacker);

        cleanupMove(attacker);
        cleanupMove(defender);

        CombatAttribute.INSTANCE.set(attacker.entity, attacker.com);
        CombatAttribute.INSTANCE.set(defender.entity, defender.com);
    }

    private static void applyScores(FighterInfo self) {
        if (self.act.action.hasTag(ActionTag.passive))
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

        if (!winner.act.action.hasTag(ActionTag.passive))
            winner.com.prevActions.add(winner.act.action.id);

        CombatNotifications.notifyAction(winner, looser);
    }

    private void finishSoloMove(FighterInfo self) {
        self.com.stage = CombatContext.Stage.END;

        applyBuffs(self.com.buffs, Effect.Stage.ACTION, self.act);
        applyEffects(self.act.action.self, Effect.Stage.ACTION, self.act, null);
        applyActionResults(self);

        if (self.act.action.hasTag(ActionTag.equip)) {
            self.com.weapon = self.entity.getHeldItem();
            self.com.armor = self.entity.getTotalArmorValue();
        } else {
//            self.com.prevActions.add(self.act.action.id);
        }

        CombatNotifications.notifySoloAction(self);

        cleanupMove(self);
        CombatAttribute.INSTANCE.set(self.entity, self.com);
    }

    private static void cleanupMove(FighterInfo self) {
        self.com.prevTarget = self.com.target;
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

        if (com.stage == CombatContext.Stage.LEAVE)
            removeFromCombat(event.player, com);
    }

    public static void removeFromCombat(EntityLivingBase entity, CombatContext com) {
        softReset(entity, com);
        com.stage = CombatContext.Stage.NONE;
    }

    public static void softReset(Entity entity, CombatContext com) {
        com.stage = com.stage.isInCombat() ? CombatContext.Stage.IDLE : CombatContext.Stage.NONE;
        com.target = 0;
        com.action = null;
        ActionAttribute.remove(entity);
    }

    public static void hardReset(Entity entity, CombatContext com) {
        softReset(entity, com);

        com.puppet = 0;

        com.knockedOut = false;
        com.buffs.clear();

        com.weapon = null;
        com.armor = 0;
        com.prevTarget = 0;
        com.prevActions.clear();

        com.stage = CombatContext.Stage.NONE;
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
