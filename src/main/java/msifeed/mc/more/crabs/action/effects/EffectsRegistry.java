package msifeed.mc.more.crabs.action.effects;

import msifeed.mc.more.crabs.character.Ability;

import java.util.HashMap;
import java.util.stream.Stream;

public enum EffectsRegistry {
    INSTANCE;

    private HashMap<String, Score> scores = new HashMap<>();
    private HashMap<String, Effect> effects = new HashMap<>();

    EffectsRegistry() {
        Stream.of(
                new Score.DiceG40(),
                new Score.DiceG40Plus(),
                new Score.DiceG40Minus()
        ).forEach(s -> scores.put(s.name(), s));
        Stream.of(Ability.values())
                .map(Score.Ability::new)
                .forEach(s -> scores.put(s.name().toLowerCase(), s));

        Stream.of(
                new Effect.Damage(),
                new Effect.SkipMove(),
                new DynamicEffect.DamageAdder(),
                new DynamicEffect.DamageMultiplier(),
                new DynamicEffect.ScoreAdder(),
                new DynamicEffect.ScoreMultiplier(),
                new DynamicEffect.MinScore(),
                new Buff()
        ).forEach(s -> effects.put(s.name(), s));
    }

    public static Score getScore(String name) {
        return INSTANCE.scores.get(name);
    }

    public static Effect getEffect(String name) {
        return INSTANCE.effects.get(name);
    }
}
