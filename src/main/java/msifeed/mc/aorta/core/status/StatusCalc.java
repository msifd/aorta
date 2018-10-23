package msifeed.mc.aorta.core.status;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.defines.data.HealthDefines;

public class StatusCalc {
    public static int maxHealth(Character c) {
        return c.bodyParts.values().stream().mapToInt(b -> b.max).sum();
    }

    public static int totalDamage(CharStatus status) {
        return status.health.values().stream()
                .mapToInt(bph -> (int) bph.health).sum();
    }

    public static int fatalDamage(Character c) {
        final int max = maxHealth(c);
        return max - (int) Math.floor(max * getFatalRate(c));
    }

    public static int damageToDeath(Character c, CharStatus status) {
        return totalDamage(status) - fatalDamage(c);
    }

    public static int disfunction(Character c, int max) {
        return (int) Math.max(1, Math.floor((double) max * c.disfunctionRate));
    }

    private static double getFatalRate(Character c) {
        final HealthDefines defines = Aorta.DEFINES.get().health;
        if (c.has(Trait.tough))
            return defines.toughFatal;
        if (c.has(Trait.weak))
            return defines.weakFatal;
        return defines.regularFatal;
    }
}
