package msifeed.mc.more.crabs.action.effects;

public abstract class DynamicEffect extends Effect {
    public abstract EffectArgs[] args();
    public abstract DynamicEffect produce(Object[] args);
    public abstract boolean equals(Effect other);

    public enum EffectArgs {
        INT, FLOAT, STRING, EFFECT
    }
}
