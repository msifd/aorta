package msifeed.mc.more.crabs.rolls;

import msifeed.mc.more.crabs.character.Ability;
import msifeed.mc.more.crabs.character.Character;

public final class Rolls {
    public static Result rollAbility(Character c, Modifiers m, Ability a) {
        final Result r = new Result();

        r.dice = Dices.n3d7m3();
        r.flatMod = m.roll;
        r.statMod = m.toAbility(a);
        r.result = r.dice + r.statMod + r.flatMod;

        return r;
    }

    public static class Result {
        public Criticalness crit = Dices.critical();
        public int dice;
        public int flatMod;
        public int statMod;
        public int result;
    }
}
