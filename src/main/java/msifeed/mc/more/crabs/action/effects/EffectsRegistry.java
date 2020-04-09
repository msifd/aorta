package msifeed.mc.more.crabs.action.effects;

import java.util.HashMap;
import java.util.stream.Stream;

public enum EffectsRegistry {
    INSTANCE;

    private HashMap<String, Effect> effects = new HashMap<>();

    EffectsRegistry() {
        Stream.of(
                // Scores
                new ScoreEffects.Roll3d7m3(),
                new ScoreEffects.ScoreAbility(),
                new ScoreEffects.ScoreAdder(),
                new ScoreEffects.ScoreMultiplier(),
                new ScoreEffects.MinScore(),
                // Actions
                new ActionEffects.Damage(),
                new ActionEffects.SkipMove(),
                new ActionEffects.DamageAdder(),
                new ActionEffects.RawDamageAdder(),
                new ActionEffects.DamageMultiplier(),
                // Buffs
                new Buff(),
                new Buff.OnRole()
        ).forEach(s -> effects.put(s.name(), s));
    }

    public static Effect getEffect(String name) {
        return INSTANCE.effects.get(name);
    }
}
