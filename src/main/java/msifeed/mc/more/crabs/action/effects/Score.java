package msifeed.mc.more.crabs.action.effects;

import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.rolls.Dices;
import msifeed.mc.more.crabs.rolls.Modifiers;

public abstract class Score {
    public abstract String name();
    public abstract int mod(Character c, Modifiers m);

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass());
    }

    // // // // // // // //

    public static class Dice3d7m3 extends Score {
        @Override
        public String name() {
            return "3d7-3";
        }

        @Override
        public int mod(Character c, Modifiers m) {
            return Dices.n3d7m3();
        }
    }

    public static class Ability extends Score {
        private final msifeed.mc.more.crabs.character.Ability ability;

        public Ability(msifeed.mc.more.crabs.character.Ability ability) {
            this.ability = ability;
        }

        @Override
        public String name() {
            return ability.toString();
        }

        @Override
        public int mod(Character c, Modifiers m) {
            return c.abilities.get(ability) + m.toAbility(ability);
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj) && this.ability.equals(((Ability) obj).ability);
        }
    }
}
