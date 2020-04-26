package msifeed.mc.more.crabs.action.effects;

import msifeed.mc.more.crabs.combat.ActionContext;
import msifeed.mc.more.crabs.combat.FighterInfo;

public abstract class Effect {
    public abstract String name();
    public abstract boolean shouldApply(Stage stage, ActionContext target, ActionContext other);
    public abstract void apply(FighterInfo target, FighterInfo other);
    public abstract boolean equals(Effect other);
    public abstract Effect clone();
    public abstract String toString();

    public enum Stage {
        PRE_SCORE, SCORE, ACTION
    }
}
