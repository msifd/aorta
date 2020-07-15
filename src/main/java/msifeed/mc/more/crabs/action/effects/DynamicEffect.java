package msifeed.mc.more.crabs.action.effects;

public interface DynamicEffect extends Effect {
    EffectArg[] args();
    DynamicEffect create(Object[] args);

    enum EffectArg {
        INT, FLOAT, STRING, EFFECT
    }
}
