package msifeed.mc.more.crabs.action.effects;

import java.util.HashMap;
import java.util.stream.Stream;

public final class EffectsRegistry {
    private static HashMap<String, Effect> EFFECTS = new HashMap<>();

    static {
        Stream.of(
                // Scores
                new ScoreEffects.Roll3d7m3(),
                new ScoreEffects.ScoreAbility(),
                new ScoreEffects.ScoreAdder(),
                new ScoreEffects.ScoreMultiplier(),
                new ScoreEffects.MinScore(),
                new ScoreEffects.ModAbility(),
                // Actions
                new ActionEffects.Damage(),
                new ActionEffects.DamageAdder(),
                new ActionEffects.RawDamageAdder(),
                new ActionEffects.DamageMultiplier(),
                new ActionEffects.OtherDamageAdder(),
                new ActionEffects.OtherDamageMultiplier(),
                new ActionEffects.Heal(),
                // Buffs
                new Buff(),
                new Buff.OnRole()
        ).forEach(s -> EFFECTS.put(s.name(), s));
    }

    public static Effect getEffect(String name) {
        return EFFECTS.get(name);
    }
}
