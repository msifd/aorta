package msifeed.mc.more.crabs.effects;

import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.character.Ability;
import msifeed.mc.more.crabs.rolls.Dices;

public abstract class Modifier {
    Modifier() {

    }

    public abstract int mod(Character c);

    public boolean isDice() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass());
    }

    public abstract String name();

    // // // // // // // //

    public static class DiceG40 extends Modifier {
        @Override
        public int mod(Character c) {
            return Dices.g40();
        }

        @Override
        public boolean isDice() {
            return true;
        }

        @Override
        public String name() {
            return "g40";
        }
    }

    public static class DiceG40Plus extends DiceG40 {
        public int mod(Character c) {
            return Dices.g40_plus();
        }

        @Override
        public String name() {
            return "g40+";
        }
    }

    public static class DiceG40Minus extends DiceG40 {
        @Override
        public int mod(Character c) {
            return Dices.g40_minus();
        }

        @Override
        public String name() {
            return "g40-";
        }
    }

    public static class Feat extends Modifier {
        private final Ability feat;

        public Feat(Ability feat) {
            this.feat = feat;
        }

        @Override
        public int mod(Character c) {
            return c.abilities.get(feat);
        }

        @Override
        public String name() {
            return feat.toString();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj) && this.feat.equals(((Feat) obj).feat);
        }
    }
}
