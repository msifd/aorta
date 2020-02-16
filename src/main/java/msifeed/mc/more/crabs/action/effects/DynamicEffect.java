package msifeed.mc.more.crabs.action.effects;

import msifeed.mc.more.crabs.combat.ActionContext;

import static msifeed.mc.more.crabs.action.effects.DynamicEffect.EffectArgs.FLOAT;
import static msifeed.mc.more.crabs.action.effects.DynamicEffect.EffectArgs.INT;

public abstract class DynamicEffect extends Effect {
    public abstract EffectArgs[] args();
    public abstract void init(Object[] args);

    // // // // // // // //

    public enum EffectArgs {
        INT, FLOAT, EFFECT
    }

    public static class DamageAdder extends DynamicEffect {
        private int value;

        @Override
        public String name() {
            return "damage+";
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
        public EffectArgs[] args() {
            return new EffectArgs[]{FLOAT};
        }

        @Override
        public void init(Object[] args) {
            value = (float) args[0];
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.ACTION;
        }

        @Override
        public void apply(Stage stage, ActionContext target, ActionContext other) {
            target.damageToReceive *= value;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }

    public static class ScoreAdder extends DynamicEffect {
        private int value;

        @Override
        public String name() {
            return "score+";
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
            return stage == Stage.SCORE;
        }

        @Override
        public void apply(Stage stage, ActionContext target, ActionContext other) {
            target.scoreEffects += value;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }

    public static class ScoreMultiplier extends DynamicEffect {
        private float value;

        @Override
        public String name() {
            return "score*";
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
            return stage == Stage.SCORE;
        }

        @Override
        public void apply(Stage stage, ActionContext target, ActionContext other) {
            target.scoreMultipliers += value;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
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
            return stage == Stage.AFTER_SCORE;
        }

        @Override
        public void apply(Stage stage, ActionContext target, ActionContext other) {
            target.successful = target.score() >= value;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }
}
