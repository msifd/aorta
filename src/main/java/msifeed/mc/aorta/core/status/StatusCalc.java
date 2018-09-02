package msifeed.mc.aorta.core.status;

import msifeed.mc.aorta.core.Core;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.traits.Trait;

public class StatusCalc {
    public static int maxHealth(Character c) {
        return c.bodyParts.stream().mapToInt(b -> b.max).sum();
    }

    public static int totalDamage(CharStatus status) {
        return status.damage.values().stream().mapToInt(Short::intValue).sum();
    }

    public static int fatalDamage(Character c) {
        final int max = maxHealth(c);
        return max - (int) Math.floor(max * getFatalRate(c));
    }

    public static int damageToDeath(Character c, CharStatus status) {
        return totalDamage(status) - fatalDamage(c);
    }

    private static double getFatalRate(Character c) {
        if (c.has(Trait.tough))
            return Core.DEFINES.health.toughFatal;
        if (c.has(Trait.weak))
            return Core.DEFINES.health.weakFatal;
        return Core.DEFINES.health.regularFatal;
    }
}
