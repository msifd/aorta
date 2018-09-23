package msifeed.mc.aorta.core.status;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.Core;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.defines.data.HealthDefines;

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
        final HealthDefines defines = Aorta.DEFINES.health;
        if (c.has(Trait.tough))
            return defines.toughFatal;
        if (c.has(Trait.weak))
            return defines.weakFatal;
        return defines.regularFatal;
    }
}
