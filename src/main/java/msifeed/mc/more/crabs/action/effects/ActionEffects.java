package msifeed.mc.more.crabs.action.effects;

import msifeed.mc.more.crabs.combat.ActionContext;
import msifeed.mc.more.crabs.combat.DamageAmount;
import msifeed.mc.more.crabs.combat.FighterInfo;
import net.minecraft.util.DamageSource;

import static msifeed.mc.more.crabs.action.effects.DynamicEffect.EffectArgs.FLOAT;
import static msifeed.mc.more.crabs.action.effects.DynamicEffect.EffectArgs.INT;

public final class ActionEffects {
    public static class Damage extends Effect {
        @Override
        public String name() {
            return "damage";
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.ACTION;
        }

        @Override
        public void apply(FighterInfo target, FighterInfo other) {
            if (other != null)
                target.act.damageToReceive.addAll(other.act.damageToDeal);
        }

        public boolean equals(Effect other) {
            return other instanceof Damage;
        }
    }

    public static class SkipMove extends Effect {
        @Override
        public String name() {
            return "skip";
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.ACTION;
        }

        @Override
        public void apply(FighterInfo target, FighterInfo other) {
            // TODO: impl skip
        }

        public boolean equals(Effect other) {
            return other instanceof SkipMove;
        }
    }

    public static class DamageAdder extends DynamicEffect {
        private static final DamageSource damageSource = new DamageSource("action");
        private int value;

        @Override
        public String name() {
            return "damage+";
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.ACTION;
        }

        @Override
        public void apply(FighterInfo target, FighterInfo other) {
            target.act.damageToReceive.add(new DamageAmount(damageSource, value));
        }

        @Override
        public boolean equals(Effect other) {
            return other instanceof DamageAdder && value == ((DamageAdder) other).value;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{INT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final DamageAdder e = new DamageAdder();
            e.value = (int) args[0];
            return e;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }

    public static class RawDamageAdder extends DynamicEffect {
        private int value;

        @Override
        public String name() {
            return "raw damage+";
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.ACTION;
        }

        @Override
        public void apply(FighterInfo target, FighterInfo other) {
            target.act.damageToReceive.add(new DamageAmount(DamageSource.generic, value));
        }

        @Override
        public boolean equals(Effect other) {
            return other instanceof RawDamageAdder && value == ((RawDamageAdder) other).value;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{INT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final RawDamageAdder e = new RawDamageAdder();
            e.value = (int) args[0];
            return e;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }

    public static class DamageMultiplier extends DynamicEffect {
        private float value;

        @Override
        public String name() {
            return "damage*";
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.ACTION;
        }

        @Override
        public void apply(FighterInfo target, FighterInfo other) {
            for (DamageAmount da : target.act.damageToReceive)
                da.amount *= value;
        }

        @Override
        public boolean equals(Effect other) {
            return other instanceof DamageMultiplier && value == ((DamageMultiplier) other).value;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{FLOAT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final DamageMultiplier e = new DamageMultiplier();
            e.value = (float) args[0];
            return e;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }
}
