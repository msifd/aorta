package msifeed.mc.more.crabs.action.effects;

import msifeed.mc.more.crabs.combat.DamageAmount;
import msifeed.mc.more.crabs.combat.FighterInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

import static msifeed.mc.more.crabs.action.effects.DynamicEffect.EffectArg.FLOAT;
import static msifeed.mc.more.crabs.action.effects.DynamicEffect.EffectArg.INT;

public final class ActionEffects {
    public static class Damage implements Effect {
        @Override
        public String name() {
            return "damage";
        }

        @Override
        public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
            return stage == Stage.ACTION;
        }

        @Override
        public void apply(Stage stage, FighterInfo target, FighterInfo other) {
            if (other != null)
                target.com.damageToReceive.addAll(target.com.damageDealt);
        }

        @Override
        public boolean same(Effect other) {
            return other instanceof Damage;
        }

        @Override
        public boolean stronger(Effect lesser) {
            return false;
        }

        @Override
        public Effect copy() {
            return new Damage();
        }

        @Override
        public String encode() {
            return name();
        }

        @Override
        public String format() {
            return "передать урон";
        }
    }

    public static class DamageAdder implements DynamicEffect {
        private int value;

        @Override
        public String name() {
            return "damage+";
        }

        @Override
        public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
            return stage == Stage.ACTION;
        }

        @Override
        public void apply(Stage stage, FighterInfo target, FighterInfo other) {
            final DamageSource ds = other == null
                    ? new DamageSource("crabs")
                    : other.entity() instanceof EntityPlayer
                        ? DamageSource.causePlayerDamage((EntityPlayer) other.entity())
                        : DamageSource.causeMobDamage(other.entity());
            target.com.damageToReceive.add(new DamageAmount(ds, value));
        }

        @Override
        public boolean same(Effect other) {
            return other instanceof DamageAdder;
        }

        @Override
        public boolean stronger(Effect lesser) {
            return lesser instanceof DamageAdder && value > ((DamageAdder) lesser).value;
        }

        @Override
        public Effect copy() {
            final DamageAdder e = new DamageAdder();
            e.value = value;
            return e;
        }

        @Override
        public String encode() {
            return name() + ':' + value;
        }

        @Override
        public String format() {
            return String.format("урон %+d", value);
        }

        @Override
        public EffectArg[] args() {
            return new EffectArg[]{INT};
        }

        @Override
        public DynamicEffect create(Object[] args) {
            final DamageAdder e = new DamageAdder();
            e.value = (int) args[0];
            return e;
        }
    }

    public static class RawDamageAdder implements DynamicEffect {
        private int value;

        @Override
        public String name() {
            return "raw damage+";
        }

        @Override
        public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
            return stage == Stage.ACTION;
        }

        @Override
        public void apply(Stage stage, FighterInfo target, FighterInfo other) {
            target.com.damageToReceive.add(new DamageAmount(DamageSource.generic, value));
        }

        @Override
        public boolean same(Effect other) {
            return other instanceof RawDamageAdder;
        }

        @Override
        public boolean stronger(Effect lesser) {
            return lesser instanceof RawDamageAdder && value > ((RawDamageAdder) lesser).value;
        }

        @Override
        public Effect copy() {
            final RawDamageAdder e = new RawDamageAdder();
            e.value = value;
            return e;
        }

        @Override
        public String encode() {
            return name() + ':' + value;
        }

        @Override
        public String format() {
            return String.format("чистый урон %+d", value);
        }

        @Override
        public EffectArg[] args() {
            return new EffectArg[]{INT};
        }

        @Override
        public DynamicEffect create(Object[] args) {
            final RawDamageAdder e = new RawDamageAdder();
            e.value = (int) args[0];
            return e;
        }
    }

    public static class DamageMultiplier implements DynamicEffect {
        private float value;

        @Override
        public String name() {
            return "damage*";
        }

        @Override
        public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
            return stage == Stage.ACTION;
        }

        @Override
        public void apply(Stage stage, FighterInfo target, FighterInfo other) {
            for (DamageAmount da : target.com.damageToReceive)
                da.amount *= value;
        }

        @Override
        public boolean same(Effect other) {
            return other instanceof DamageMultiplier;
        }

        @Override
        public boolean stronger(Effect lesser) {
            return lesser instanceof DamageMultiplier && value > ((DamageMultiplier) lesser).value;
        }

        @Override
        public Effect copy() {
            final DamageMultiplier e = new DamageMultiplier();
            e.value = value;
            return e;
        }

        @Override
        public String encode() {
            return name() + ':' + value;
        }

        @Override
        public String format() {
            return String.format("урон * %.2f", value);
        }

        @Override
        public EffectArg[] args() {
            return new EffectArg[]{FLOAT};
        }

        @Override
        public DynamicEffect create(Object[] args) {
            final DamageMultiplier e = new DamageMultiplier();
            e.value = (float) args[0];
            return e;
        }
    }

    public static class OtherDamageAdder implements DynamicEffect {
        private int value;

        @Override
        public String name() {
            return "other damage+";
        }

        @Override
        public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
            return stage == Stage.ACTION;
        }

        @Override
        public void apply(Stage stage, FighterInfo target, FighterInfo other) {
            final DamageSource ds = target.entity() instanceof EntityPlayer
                    ? DamageSource.causePlayerDamage((EntityPlayer) target.entity())
                    : DamageSource.causeMobDamage(target.entity());
            other.com.damageToReceive.add(new DamageAmount(ds, value));
        }

        @Override
        public boolean same(Effect other) {
            return other instanceof OtherDamageAdder;
        }

        @Override
        public boolean stronger(Effect lesser) {
            return lesser instanceof OtherDamageAdder && value > ((OtherDamageAdder) lesser).value;
        }

        @Override
        public Effect copy() {
            final OtherDamageAdder e = new OtherDamageAdder();
            e.value = value;
            return e;
        }

        @Override
        public String encode() {
            return name() + ':' + value;
        }

        @Override
        public String format() {
            return String.format("чужой урон %+d", value);
        }

        @Override
        public EffectArg[] args() {
            return new EffectArg[]{INT};
        }

        @Override
        public DynamicEffect create(Object[] args) {
            final OtherDamageAdder e = new OtherDamageAdder();
            e.value = (int) args[0];
            return e;
        }
    }

    public static class OtherDamageMultiplier implements DynamicEffect {
        private float value;

        @Override
        public String name() {
            return "other damage*";
        }

        @Override
        public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
            return stage == Stage.ACTION;
        }

        @Override
        public void apply(Stage stage, FighterInfo target, FighterInfo other) {
            for (DamageAmount da : other.com.damageToReceive)
                da.amount *= value;
        }

        @Override
        public boolean same(Effect other) {
            return other instanceof OtherDamageMultiplier;
        }

        @Override
        public boolean stronger(Effect lesser) {
            return lesser instanceof OtherDamageMultiplier && value > ((OtherDamageMultiplier) lesser).value;
        }

        @Override
        public Effect copy() {
            final OtherDamageMultiplier e = new OtherDamageMultiplier();
            e.value = value;
            return e;
        }

        @Override
        public String encode() {
            return name() + ':' + value;
        }

        @Override
        public String format() {
            return String.format("чужой урон * %.2f", value);
        }

        @Override
        public EffectArg[] args() {
            return new EffectArg[]{FLOAT};
        }

        @Override
        public DynamicEffect create(Object[] args) {
            final OtherDamageMultiplier e = new OtherDamageMultiplier();
            e.value = (float) args[0];
            return e;
        }
    }

    public static class Heal implements DynamicEffect {
        private int value;

        @Override
        public String name() {
            return "heal";
        }

        @Override
        public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
            return stage == Stage.ACTION;
        }

        @Override
        public void apply(Stage stage, FighterInfo target, FighterInfo other) {
            target.entity().heal(value);
        }

        @Override
        public boolean same(Effect other) {
            return other instanceof Heal;
        }

        @Override
        public boolean stronger(Effect lesser) {
            return lesser instanceof Heal && value > ((Heal) lesser).value;
        }

        @Override
        public Effect copy() {
            final Heal e = new Heal();
            e.value = value;
            return e;
        }

        @Override
        public String encode() {
            return name() + ':' + value;
        }

        @Override
        public String format() {
            return String.format("излечить %d", value);
        }

        @Override
        public EffectArg[] args() {
            return new EffectArg[]{INT};
        }

        @Override
        public DynamicEffect create(Object[] args) {
            final Heal e = new Heal();
            e.value = (int) args[0];
            return e;
        }
    }
}
