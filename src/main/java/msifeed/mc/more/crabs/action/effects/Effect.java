package msifeed.mc.more.crabs.action.effects;

import msifeed.mc.more.crabs.combat.ActionContext;

public abstract class Effect {
    public abstract String name();

    public abstract boolean shouldApply(Stage stage, ActionContext target, ActionContext other);

    public abstract void apply(Stage stage, ActionContext target, ActionContext other);

    @Override
    public String toString() {
        return name();
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass());
    }

    // // // // // // // //

    public enum Stage {
        SCORE, AFTER_SCORE, ACTION, AFTER_ACTION
    }

    // // // // // // // //

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
        public void apply(Stage stage, ActionContext target, ActionContext other) {
            target.damageToReceive += other.damageToDeal;
        }
    }

    // // // // // // // //

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
        public void apply(Stage stage, ActionContext target, ActionContext other) {
            target.damageToReceive += other.damageToDeal;
        }
    }
}
