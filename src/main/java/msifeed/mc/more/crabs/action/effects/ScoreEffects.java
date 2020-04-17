package msifeed.mc.more.crabs.action.effects;

import msifeed.mc.more.More;
import msifeed.mc.more.crabs.character.Ability;
import msifeed.mc.more.crabs.combat.ActionContext;
import msifeed.mc.more.crabs.combat.FighterInfo;
import msifeed.mc.more.crabs.rolls.Dices;
import net.minecraft.util.MathHelper;

import static msifeed.mc.more.crabs.action.effects.DynamicEffect.EffectArgs.*;

public final class ScoreEffects {
    public static class Roll3d7m3 extends Effect {
        @Override
        public String name() {
            return "3d7-3";
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.SCORE;
        }

        @Override
        public void apply(FighterInfo target, FighterInfo other) {
            final float penaltyRate = More.DEFINES.combat().armorPenalty.onRoll;
            final float penalty = penaltyRate * target.entity.getTotalArmorValue();
            target.act.scoreAction += Dices.n3d7m3() - penalty;
        }

        @Override
        public boolean equals(Effect other) {
            return other instanceof Roll3d7m3;
        }

        @Override
        public String toString() {
            return name();
        }
    }

    public static class ScoreAbility extends DynamicEffect {
        private Ability ability;
        private float multiplier;

        @Override
        public String name() {
            return "ability";
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.SCORE;
        }

        @Override
        public void apply(FighterInfo target, FighterInfo other) {
            final float mod = target.mod.toAbility(ability);
            final float value = target.chr.abilities.getOrDefault(ability, 0);
            final float penaltyRate = More.DEFINES.combat().armorPenalty.onStats.getOrDefault(ability, 0f);
            final float penalty = penaltyRate * target.entity.getTotalArmorValue();

            target.act.scoreAction += MathHelper.floor_float((mod + value - penalty) * multiplier);
        }

        @Override
        public boolean equals(Effect other) {
            return other instanceof ScoreAbility
                    && ability == ((ScoreAbility) other).ability
                    && multiplier == ((ScoreAbility) other).multiplier;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{STRING, FLOAT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final ScoreAbility e = new ScoreAbility();
            e.ability = Ability.valueOf(((String) args[0]).toUpperCase());
            e.multiplier = (float) args[1];
            return e;
        }

        @Override
        public String toString() {
            return name() + ':' + ability + ':' + multiplier;
        }
    }

    public static class ScoreAdder extends DynamicEffect {
        private int value;

        @Override
        public String name() {
            return "score+";
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.SCORE;
        }

        @Override
        public void apply(FighterInfo target, FighterInfo other) {
            target.act.scoreAction += value;
        }

        @Override
        public boolean equals(Effect other) {
            return other instanceof ScoreAdder && value == ((ScoreAdder) other).value;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{INT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final ScoreAdder e = new ScoreAdder();
            e.value = (int) args[0];
            return e;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }

    public static class ScoreMultiplier extends DynamicEffect {
        private float value;

        @Override
        public String name() {
            return "score*";
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.SCORE;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{FLOAT};
        }

        @Override
        public boolean equals(Effect other) {
            return other instanceof ScoreMultiplier && value == ((ScoreMultiplier) other).value;
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final ScoreMultiplier e = new ScoreMultiplier();
            e.value = (float) args[0];
            return e;
        }

        @Override
        public void apply(FighterInfo target, FighterInfo other) {
            target.act.scoreAction = MathHelper.floor_float(target.act.scoreAction * value);
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }

    public static class MinScore extends DynamicEffect {
        private int value;

        @Override
        public String name() {
            return "min score";
        }

        @Override
        public boolean shouldApply(Stage stage, ActionContext target, ActionContext other) {
            return stage == Stage.SCORE;
        }

        @Override
        public void apply(FighterInfo target, FighterInfo other) {
            target.act.successful = target.act.score() >= value;
        }

        @Override
        public boolean equals(Effect other) {
            return other instanceof MinScore && value == ((MinScore) other).value;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{INT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final MinScore e = new MinScore();
            e.value = (int) args[0];
            return e;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }
}
