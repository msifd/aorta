package msifeed.mc.more.crabs.action.effects;

import msifeed.mc.more.crabs.combat.ActionContext;
import msifeed.mc.more.crabs.combat.DamageAmount;
import net.minecraft.util.DamageSource;

import static msifeed.mc.more.crabs.action.effects.DynamicEffect.EffectArgs.FLOAT;
import static msifeed.mc.more.crabs.action.effects.DynamicEffect.EffectArgs.INT;

public abstract class DynamicEffect extends Effect {
    public abstract EffectArgs[] args();
    public abstract DynamicEffect produce(Object[] args);

    // // // // // // // //

    public enum EffectArgs {
        INT, FLOAT, STRING, EFFECT
    }

    public static class DamageAdder extends DynamicEffect {
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
        public void apply(ActionContext target, ActionContext other) {
            target.damageToReceive.add(new DamageAmount(DamageSource.generic, value));
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
        public void apply(ActionContext target, ActionContext other) {
            for (DamageAmount da : target.damageToReceive)
                da.amount *= value;
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

    public static class ScoreAdder extends DynamicEffect {
        private int value;

        @Override
        public String name() {
            return "score+";
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.SCORE;
        }

        @Override
        public void apply(ActionContext target, ActionContext other) {
            target.scoreEffects += value;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{INT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final ScoreAdder e = new ScoreAdder();
            e.value = (int) args[0];
            return e;
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
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.SCORE;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{FLOAT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final ScoreMultiplier e = new ScoreMultiplier();
            e.value = (float) args[0];
            return e;
        }

        @Override
        public void apply(ActionContext target, ActionContext other) {
            target.scoreMultiplier = value;
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
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.SCORE;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{INT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final MinScore e = new MinScore();
            e.value = (int) args[0];
            return e;
        }

        @Override
        public void apply(ActionContext target, ActionContext other) {
            target.successful = target.score() >= value;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }
}
