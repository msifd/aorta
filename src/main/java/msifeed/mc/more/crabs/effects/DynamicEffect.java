package msifeed.mc.more.crabs.effects;

import msifeed.mc.more.crabs.combat.ActionContext;
import msifeed.mc.sys.utils.L10n;

import static msifeed.mc.more.crabs.effects.DynamicEffect.EffectArgs.FLOAT;
import static msifeed.mc.more.crabs.effects.DynamicEffect.EffectArgs.INT;

public abstract class DynamicEffect extends Effect {
    public abstract EffectArgs[] args();

    public abstract void init(Object[] args);

    // // // // // // // //

    public enum EffectArgs {
        INT, FLOAT, EFFECT
    }

    public static class ConstDamage extends DynamicEffect {
        private int value;

        @Override
        public String name() {
            return "const_damage";
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{INT};
        }

        @Override
        public void init(Object[] args) {
            value = (int) args[0];
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.ACTION;
        }

        @Override
        public void apply(Stage stage, ActionContext target, ActionContext other) {
            target.damageToReceive += value;
        }
    }

    public static class Score extends DynamicEffect {
        private int value;

        @Override
        public String name() {
            return "score";
        }

        @Override
        public String toString() {
            return L10n.fmt("misca.crabs.buff.score", value);
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{INT};
        }

        @Override
        public void init(Object[] args) {
            value = (int) args[0];
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.BEFORE_MODS;
        }

        @Override
        public void apply(Stage stage, ActionContext target, ActionContext other) {
            target.effectsScore += value;
        }
    }

    public static class MinScore extends DynamicEffect {
        private int value;

        @Override
        public String name() {
            return "min_score";
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{INT};
        }

        @Override
        public void init(Object[] args) {
            value = (int) args[0];
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.AFTER_MODS;
        }

        @Override
        public void apply(Stage stage, ActionContext target, ActionContext other) {
            target.successful = target.score() >= value;
        }
    }

    // // // // // // // //

    public static class ReceivedDamageMultiplier extends DynamicEffect {
        private float value;

        @Override
        public String name() {
            return "received_damage_mult";
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{FLOAT};
        }

        @Override
        public void init(Object[] args) {
            value = (float) args[0];
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.AFTER_ACTION;
        }

        @Override
        public void apply(Stage stage, ActionContext target, ActionContext other) {
            target.damageToReceive *= value;
        }
    }
}
