package msifeed.mc.more.crabs.action.effects;

import msifeed.mc.more.More;
import msifeed.mc.more.crabs.character.Ability;
import msifeed.mc.more.crabs.combat.FighterInfo;
import msifeed.mc.more.crabs.rolls.Dices;
import net.minecraft.util.MathHelper;

import static msifeed.mc.more.crabs.action.effects.DynamicEffect.EffectArg.*;

public final class ScoreEffects {
    public static class Roll3d7m3 implements Effect {
        @Override
        public String name() {
            return "3d7-3";
        }

        @Override
        public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
            return stage == Stage.SCORE;
        }

        @Override
        public void apply(Stage stage, FighterInfo target, FighterInfo other) {
            final float penaltyRate = More.DEFINES.combat().armorPenalty.onRoll;
            final float penalty = penaltyRate * target.entity.getTotalArmorValue();
            target.act.scoreAction += Dices.n3d7m3() - penalty;
        }

        @Override
        public boolean same(Effect other) {
            return other instanceof Roll3d7m3;
        }

        @Override
        public boolean stronger(Effect lesser) {
            return false;
        }

        @Override
        public Effect copy() {
            return new Roll3d7m3();
        }

        @Override
        public String encode() {
            return name();
        }
    }

    public static class ScoreAbility implements DynamicEffect {
        private Ability ability;
        private float multiplier;

        @Override
        public String name() {
            return "ability";
        }

        @Override
        public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
            return stage == Stage.SCORE;
        }

        @Override
        public void apply(Stage stage, FighterInfo target, FighterInfo other) {
            final float mod = target.mod.toAbility(ability) + target.act.effectAbilityMods.getOrDefault(ability, 0);
            final float value = target.chr.abilities.getOrDefault(ability, 0);
            final float penaltyRate = More.DEFINES.combat().armorPenalty.onStats.getOrDefault(ability, 0f);
            final float penalty = penaltyRate * target.entity.getTotalArmorValue();

            target.act.scoreAction += MathHelper.floor_float((mod + value - penalty) * multiplier);
        }

        @Override
        public boolean same(Effect other) {
            return other instanceof ScoreAbility;
        }

        @Override
        public boolean stronger(Effect lesser) {
            return lesser instanceof ScoreAbility
                    && ability == ((ScoreAbility) lesser).ability
                    && multiplier > ((ScoreAbility) lesser).multiplier;
        }

        @Override
        public Effect copy() {
            final ScoreAbility e = new ScoreAbility();
            e.ability = ability;
            e.multiplier = multiplier;
            return e;
        }

        @Override
        public String encode() {
            return name() + ':' + ability + ':' + multiplier;
        }

        @Override
        public EffectArg[] args() {
            return new EffectArg[]{STRING, FLOAT};
        }

        @Override
        public DynamicEffect create(Object[] args) {
            final ScoreAbility e = new ScoreAbility();
            e.ability = Ability.valueOf(((String) args[0]).toUpperCase());
            e.multiplier = (float) args[1];
            return e;
        }
    }

    public static class ScoreAdder implements DynamicEffect {
        private int value;

        @Override
        public String name() {
            return "score+";
        }

        @Override
        public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
            return stage == Stage.SCORE;
        }

        @Override
        public void apply(Stage stage, FighterInfo target, FighterInfo other) {
            target.act.scoreAction += value;
        }

        @Override
        public boolean same(Effect other) {
            return other instanceof ScoreAdder;
        }

        @Override
        public boolean stronger(Effect lesser) {
            return lesser instanceof ScoreAdder && value > ((ScoreAdder) lesser).value;
        }

        @Override
        public Effect copy() {
            final ScoreAdder e = new ScoreAdder();
            e.value = value;
            return e;
        }

        @Override
        public String encode() {
            return name() + ':' + value;
        }

        @Override
        public EffectArg[] args() {
            return new EffectArg[]{INT};
        }

        @Override
        public DynamicEffect create(Object[] args) {
            final ScoreAdder e = new ScoreAdder();
            e.value = (int) args[0];
            return e;
        }
    }

    public static class ScoreMultiplier implements DynamicEffect {
        private float value;

        @Override
        public String name() {
            return "score*";
        }

        @Override
        public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
            return stage == Stage.SCORE;
        }

        @Override
        public void apply(Stage stage, FighterInfo target, FighterInfo other) {
            target.act.scoreAction = MathHelper.floor_float(target.act.scoreAction * value);
        }

        @Override
        public boolean same(Effect other) {
            return other instanceof ScoreMultiplier;
        }

        @Override
        public boolean stronger(Effect lesser) {
            return lesser instanceof ScoreMultiplier && value > ((ScoreMultiplier) lesser).value;
        }

        @Override
        public Effect copy() {
            final ScoreMultiplier e = new ScoreMultiplier();
            e.value = value;
            return e;
        }

        @Override
        public String encode() {
            return name() + ':' + value;
        }

        @Override
        public EffectArg[] args() {
            return new EffectArg[]{FLOAT};
        }

        @Override
        public DynamicEffect create(Object[] args) {
            final ScoreMultiplier e = new ScoreMultiplier();
            e.value = (float) args[0];
            return e;
        }
    }

    public static class MinScore implements DynamicEffect {
        private int value;

        @Override
        public String name() {
            return "min score";
        }

        @Override
        public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
            return stage == Stage.SCORE;
        }

        @Override
        public void apply(Stage stage, FighterInfo target, FighterInfo other) {
            target.act.successful = target.act.score() >= value;
        }

        @Override
        public boolean same(Effect other) {
            return other instanceof MinScore;
        }

        @Override
        public boolean stronger(Effect lesser) {
            // Inverted!
            return lesser instanceof MinScore && value < ((MinScore) lesser).value;
        }

        @Override
        public Effect copy() {
            final MinScore e = new MinScore();
            e.value = value;
            return e;
        }

        @Override
        public String encode() {
            return name() + ':' + value;
        }

        @Override
        public EffectArg[] args() {
            return new EffectArg[]{INT};
        }

        @Override
        public DynamicEffect create(Object[] args) {
            final MinScore e = new MinScore();
            e.value = (int) args[0];
            return e;
        }
    }

    public static class ModAbility implements DynamicEffect {
        private Ability ability;
        private int modifier;

        @Override
        public String name() {
            return "mod ability";
        }

        @Override
        public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
            return stage == Stage.PRE_SCORE;
        }

        @Override
        public void apply(Stage stage, FighterInfo target, FighterInfo other) {
            target.act.effectAbilityMods.compute(ability, (k, v) -> (v == null) ? modifier : v + modifier);
        }

        @Override
        public boolean same(Effect other) {
            return other instanceof ModAbility;
        }

        @Override
        public boolean stronger(Effect lesser) {
            return lesser instanceof ModAbility
                    && ability == ((ModAbility) lesser).ability
                    && modifier > ((ModAbility) lesser).modifier;
        }

        @Override
        public Effect copy() {
            final ModAbility e = new ModAbility();
            e.ability = ability;
            e.modifier = modifier;
            return e;
        }

        @Override
        public String encode() {
            return name() + ':' + ability + ':' + modifier;
        }

        @Override
        public EffectArg[] args() {
            return new EffectArg[]{STRING, INT};
        }

        @Override
        public DynamicEffect create(Object[] args) {
            final ModAbility e = new ModAbility();
            e.ability = Ability.valueOf(((String) args[0]).toUpperCase());
            e.modifier = (int) args[1];
            return e;
        }
    }
}
