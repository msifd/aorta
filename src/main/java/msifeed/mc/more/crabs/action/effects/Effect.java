package msifeed.mc.more.crabs.action.effects;

import msifeed.mc.more.crabs.combat.ActionContext;

public abstract class Effect {
    public abstract String name();
    public abstract boolean shouldApply(Stage stage, ActionContext target, ActionContext other);
    public abstract void apply(ActionContext target, ActionContext other);
    public abstract boolean equals(Effect other);

    @Override
    public String toString() {
        return name();
    }

    // // // // // // // //

    public enum Stage {
        SCORE, ACTION
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
        public void apply(ActionContext target, ActionContext other) {
            if (other != null)
                target.damageToReceive.addAll(other.damageToDeal);
        }

        public boolean equals(Effect other) {
            return other instanceof Damage;
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
        public void apply(ActionContext target, ActionContext other) {
            // TODO: impl skip
        }

        public boolean equals(Effect other) {
            return other instanceof SkipMove;
        }
    }
}
