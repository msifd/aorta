package msifeed.mc.more.crabs.action.effects;

import msifeed.mc.more.crabs.action.ActionTag;
import msifeed.mc.more.crabs.combat.CombatContext;
import msifeed.mc.more.crabs.combat.FighterInfo;

import java.util.List;

import static msifeed.mc.more.crabs.action.effects.DynamicEffect.EffectArgs.*;

public final class Buff extends DynamicEffect {
    private int pause;
    private int steps;
    private RepeatMode repeatMode;
    private Effect effect;

    @Override
    public String name() {
        return "buff";
    }

    @Override
    public String toString() {
        return name() + ':' + pause + ':' + steps + ':' + repeatMode + ':' + effect.toString();
    }

    public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
        if (other == null)
            return effect.shouldApply(stage, target, null);
        else
            return true;
    }

    /// Call from action effects - add to buffs
    @Override
    public void apply(FighterInfo target, FighterInfo other) {
        target.act.buffsToReceive.add(this);
    }

    /// Call from buffs - apply effect
    public void applyEffect(FighterInfo target, FighterInfo other) {
        if (active())
            effect.apply(target, other);
        step();
    }

    private boolean active() {
        return started() && !ended();
    }

    private boolean started() {
        return pause <= 0;
    }

    public boolean ended() {
        return steps <= 0;
    }

    private void step() {
        if (started())
            steps--;
        else
            pause--;
    }

    @Override
    public boolean equals(Effect other) {
        return other instanceof Buff && effect.equals(((Buff) other).effect);
    }

    @Override
    public Effect clone() {
        final Buff b = new Buff();
        b.pause = pause;
        b.steps = steps;
        b.repeatMode = repeatMode;
        b.effect = effect.clone();
        return b;
    }

    /**
     * [pause before activation]:[number of time the effect is applied]:[repeat mode]:[effect]
     */
    @Override
    public EffectArgs[] args() {
        return new EffectArgs[]{INT, INT, STRING, EFFECT};
    }

    @Override
    public DynamicEffect produce(Object[] args) {
        final Buff b = new Buff();
        b.pause = (int) args[0];
        b.steps = (int) args[1];
        b.repeatMode = RepeatMode.valueOf(((String) args[2]).toLowerCase());
        b.effect = ((Effect) args[3]).clone();
        return b;
    }

    public static void mergeBuff(List<Buff> current, Buff buff) {
        final Buff cur = current.stream()
                .filter(b -> b.effect.equals(buff.effect))
                .findAny().orElse(null);
        if (cur != null) {
            switch (buff.repeatMode) {
                case extend:
                    cur.steps += buff.steps;
                    return;
                case replace:
                    cur.pause = buff.pause;
                    cur.steps = buff.steps;
                    return;
            }
        }
        // If there are no current buffs with this effect or it have stack mode
        current.add(buff);
    }

    private enum RepeatMode {
        extend, replace, stack
    }

    public static class OnRole extends DynamicEffect {
        private CombatContext.Role role;
        private Effect effect;

        @Override
        public String name() {
            return "role";
        }

        @Override
        public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
            return role == target.com.role
                    && effect.shouldApply(stage, target, other);
        }

        @Override
        public void apply(FighterInfo target, FighterInfo other) {
            this.effect.apply(target, other);
        }

        @Override
        public boolean equals(Effect other) {
            return other instanceof OnRole
                    && ((OnRole) other).role == this.role
                    && ((OnRole) other).effect.equals(this.effect);
        }

        @Override
        public Effect clone() {
            final OnRole e = new OnRole();
            e.role = role;
            e.effect = effect.clone();
            return e;
        }

        @Override
        public String toString() {
            return name() + ':' + role.toString().toLowerCase() + ':' + effect.toString();
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{STRING, EFFECT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final OnRole e = new OnRole();
            e.role = CombatContext.Role.valueOf(((String) args[0]).toUpperCase());
            e.effect = (Effect) args[1];
            return e;
        }
    }

    public static class OnTag extends DynamicEffect {
        private ActionTag tag;
        private Effect effect;

        @Override
        public String name() {
            return "tag";
        }

        @Override
        public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
            return target.com.action.hasAnyTag(tag)
                    && effect.shouldApply(stage, target, other);
        }

        @Override
        public void apply(FighterInfo target, FighterInfo other) {
            this.effect.apply(target, other);
        }

        @Override
        public boolean equals(Effect other) {
            return other instanceof OnTag
                    && ((OnTag) other).tag == this.tag
                    && ((OnTag) other).effect.equals(this.effect);
        }

        @Override
        public Effect clone() {
            final OnTag e = new OnTag();
            e.tag = tag;
            e.effect = effect.clone();
            return e;
        }

        @Override
        public String toString() {
            return name() + ':' + tag.toString().toLowerCase() + ':' + effect.toString();
        }

        @Override
        public EffectArgs[] args() {
            return new EffectArgs[]{STRING, EFFECT};
        }

        @Override
        public DynamicEffect produce(Object[] args) {
            final OnTag e = new OnTag();
            e.tag = ActionTag.valueOf(((String) args[0]).toLowerCase());
            e.effect = (Effect) args[1];
            return e;
        }
    }
}
