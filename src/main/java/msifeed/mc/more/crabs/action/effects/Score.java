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

    public static class DiceG40 extends Score {
        @Override
        public String name() {
            return "g40";
        }

        @Override
        public int mod(Character c, Modifiers m) {
            return Dices.g40();
        }
    }

    public static class DiceG40Plus extends DiceG40 {
        @Override
        public String name() {
            return "g40+";
        }

        public int mod(Character c, Modifiers m) {
            return Dices.g40_plus();
        }
    }

    public static class DiceG40Minus extends DiceG40 {
        @Override
        public String name() {
            return "g40-";
        }

        @Override
        public int mod(Character c, Modifiers m) {
            return Dices.g40_minus();
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
            return c.abilities.get(ability) + m.features.getOrDefault(ability, 0);
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj) && this.ability.equals(((Ability) obj).ability);
        }
    }
}
