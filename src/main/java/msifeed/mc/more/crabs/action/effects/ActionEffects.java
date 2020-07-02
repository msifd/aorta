package msifeed.mc.more.crabs.action.effects;

import msifeed.mc.more.crabs.combat.DamageAmount;
import msifeed.mc.more.crabs.combat.FighterInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

import static msifeed.mc.more.crabs.action.effects.DynamicEffect.EffectArgs.FLOAT;
import static msifeed.mc.more.crabs.action.effects.DynamicEffect.EffectArgs.INT;

public final class ActionEffects {
    public static class Damage extends Effect {
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
        public boolean equals(Effect other) {
            return other instanceof Damage;
        }

        @Override
        public Effect clone() {
            return new Damage();
        }

        @Override
        public String toString() {
            return name();
        }
    }

    public static class DamageAdder extends DynamicEffect {
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
                    : other.entity instanceof EntityPlayer
                        ? DamageSource.causePlayerDamage((EntityPlayer) other.entity)
                        : DamageSource.causeMobDamage(other.entity);
            target.com.damageToReceive.add(new DamageAmount(ds, value));
        }

        @Override
        public boolean equals(Effect other) {
            return other instanceof DamageAdder && value == ((DamageAdder) other).value;
        }

        @Override
        public Effect clone() {
            final DamageAdder e = new DamageAdder();
            e.value = value;
            return e;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{INT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final DamageAdder e = new DamageAdder();
            e.value = (int) args[0];
            return e;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }

    public static class RawDamageAdder extends DynamicEffect {
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
        public boolean equals(Effect other) {
            return other instanceof RawDamageAdder && value == ((RawDamageAdder) other).value;
        }

        @Override
        public Effect clone() {
            final RawDamageAdder e = new RawDamageAdder();
            e.value = value;
            return e;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{INT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final RawDamageAdder e = new RawDamageAdder();
            e.value = (int) args[0];
            return e;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }

    public static class DamageMultiplier extends DynamicEffect {
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
        public boolean equals(Effect other) {
            return other instanceof DamageMultiplier && value == ((DamageMultiplier) other).value;
        }

        @Override
        public Effect clone() {
            final DamageMultiplier e = new DamageMultiplier();
            e.value = value;
            return e;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{FLOAT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final DamageMultiplier e = new DamageMultiplier();
            e.value = (float) args[0];
            return e;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }

    public static class OtherDamageAdder extends DynamicEffect {
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
            final DamageSource ds = target.entity instanceof EntityPlayer
                    ? DamageSource.causePlayerDamage((EntityPlayer) target.entity)
                    : DamageSource.causeMobDamage(target.entity);
            other.com.damageToReceive.add(new DamageAmount(ds, value));
        }

        @Override
        public boolean equals(Effect other) {
            return other instanceof OtherDamageAdder && value == ((OtherDamageAdder) other).value;
        }

        @Override
        public Effect clone() {
            final OtherDamageAdder e = new OtherDamageAdder();
            e.value = value;
            return e;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{INT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final OtherDamageAdder e = new OtherDamageAdder();
            e.value = (int) args[0];
            return e;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }

    public static class OtherDamageMultiplier extends DynamicEffect {
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
        public boolean equals(Effect other) {
            return other instanceof OtherDamageMultiplier && value == ((OtherDamageMultiplier) other).value;
        }

        @Override
        public Effect clone() {
            final OtherDamageMultiplier e = new OtherDamageMultiplier();
            e.value = value;
            return e;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{FLOAT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final OtherDamageMultiplier e = new OtherDamageMultiplier();
            e.value = (float) args[0];
            return e;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }

    public static class Heal extends DynamicEffect {
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
            target.entity.heal(value);
        }

        @Override
        public boolean equals(Effect other) {
            return other instanceof Heal && value == ((Heal) other).value;
        }

        @Override
        public Effect clone() {
            final Heal e = new Heal();
            e.value = value;
            return e;
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{INT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final Heal e = new Heal();
            e.value = (int) args[0];
            return e;
        }

        @Override
        public String toString() {
            return name() + ':' + value;
        }
    }
}
