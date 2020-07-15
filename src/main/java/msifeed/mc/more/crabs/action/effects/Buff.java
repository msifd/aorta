package msifeed.mc.more.crabs.action.effects;

import msifeed.mc.more.crabs.action.ActionTag;
import msifeed.mc.more.crabs.combat.CombatContext;
import msifeed.mc.more.crabs.combat.FighterInfo;

import java.util.List;
import java.util.stream.Collectors;

import static msifeed.mc.more.crabs.action.effects.DynamicEffect.EffectArg.*;

public final class Buff implements DynamicEffect {
    public int pause = 0;
    public int steps = 1;
    public MergeMode mergeMode = MergeMode.replace;
    public Effect effect;

    @Override
    public String name() {
        return "buff";
    }

    public boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other) {
        if (other == null) // From applyScores
            return effect.shouldApply(stage, target, null);
        else
            return true;
    }

    /// Call from action effects - add to buffs
    @Override
    public void apply(Stage stage, FighterInfo target, FighterInfo other) {
        if (stage == Stage.ACTION) // From applyEffectsAndResults
            target.act.buffsToReceive.add((Buff) this.copy());
    }

    /// Call from buffs - apply effect
    public void applyEffect(Stage stage, FighterInfo target, FighterInfo other) {
        if (active())
            effect.apply(stage, target, other);
        step();
    }

    @Override
    public boolean same(Effect other) {
        return other instanceof Buff && ((Buff) other).effect.same(effect);
    }

    @Override
    public boolean stronger(Effect lesser) {
        return lesser instanceof Buff && ((Buff) lesser).effect.stronger(effect);
    }

    @Override
    public Effect copy() {
        final Buff b = new Buff();
        b.pause = pause;
        b.steps = steps;
        b.mergeMode = mergeMode;
        b.effect = effect.copy();
        return b;
    }

    @Override
    public String encode() {
        return name() + ':' + pause + ':' + steps + ':' + mergeMode + ':' + effect.encode();
    }

    /**
     * [pause before activation]:[number of time the effect is applied]:[repeat mode]:[effect]
     */
    @Override
    public EffectArg[] args() {
        return new EffectArg[]{INT, INT, STRING, EFFECT};
    }

    @Override
    public DynamicEffect create(Object[] args) {
        final Buff b = new Buff();
        b.pause = (int) args[0];
        b.steps = (int) args[1];
        b.mergeMode = MergeMode.valueOf(((String) args[2]).toLowerCase());
        b.effect = ((Effect) args[3]).copy();
        return b;
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

    public enum MergeMode {
        extend, replace, stack
    }

    public static void mergeBuff(List<Buff> current, Buff buff) {
        final List<Buff> sameBuffs = current.stream()
                .filter(buff::same)
                .collect(Collectors.toList());

        if (sameBuffs.isEmpty()) {
            current.add(buff);
            return;
        }

        switch (buff.mergeMode) {
            case extend: {
                final Buff b = sameBuffs.get(0);
                b.steps = Math.max(b.steps, buff.steps);
                break;
            }
            case replace:
                final List<Buff> strongerBuffs = current.stream().filter(buff::same).collect(Collectors.toList());
                if (!strongerBuffs.isEmpty()) {
                    final Buff b = strongerBuffs.get(0);
                    b.pause = Math.min(b.pause, buff.pause);
                    b.steps = buff.steps;
                    b.effect = buff.effect;
                } else {
                    // If no stronger buffs where found, extend same one
                    final Buff b = sameBuffs.get(0);
                    b.steps = Math.max(b.steps, buff.steps);
                }
                break;
            case stack:
                current.add(buff);
                break;
        }
    }

    public static class OnRole implements DynamicEffect {
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
        public void apply(Stage stage, FighterInfo target, FighterInfo other) {
            this.effect.apply(stage, target, other);
        }

        @Override
        public boolean same(Effect other) {
            return other instanceof OnRole
                    && ((OnRole) other).role == role
                    && ((OnRole) other).effect.same(effect);
        }

        @Override
        public boolean stronger(Effect lesser) {
            return lesser instanceof OnRole
                    && ((OnRole) lesser).role == role
                    && ((OnRole) lesser).effect.stronger(effect);
        }

        @Override
        public Effect copy() {
            final OnRole e = new OnRole();
            e.role = role;
            e.effect = effect.copy();
            return e;
        }

        @Override
        public String encode() {
            return name() + ':' + role.toString().toLowerCase() + ':' + effect.encode();
        }

        @Override
        public EffectArg[] args() {
            return new EffectArg[]{STRING, EFFECT};
        }

        @Override
        public DynamicEffect create(Object[] args) {
            final OnRole e = new OnRole();
            e.role = CombatContext.Role.valueOf(((String) args[0]).toUpperCase());
            e.effect = (Effect) args[1];
            return e;
        }
    }

    public static class OnTag implements DynamicEffect {
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
        public void apply(Stage stage, FighterInfo target, FighterInfo other) {
            this.effect.apply(stage, target, other);
        }

        @Override
        public boolean same(Effect other) {
            return other instanceof OnTag
                    && ((OnTag) other).tag == tag
                    && ((OnTag) other).effect.same(effect);
        }

        @Override
        public boolean stronger(Effect lesser) {
            return lesser instanceof OnTag
                    && ((OnTag) lesser).tag == tag
                    && ((OnTag) lesser).effect.stronger(effect);
        }

        @Override
        public Effect copy() {
            final OnTag e = new OnTag();
            e.tag = tag;
            e.effect = effect.copy();
            return e;
        }

        @Override
        public String encode() {
            return name() + ':' + tag.toString().toLowerCase() + ':' + effect.encode();
        }

        @Override
        public EffectArg[] args() {
            return new EffectArg[]{STRING, EFFECT};
        }

        @Override
        public DynamicEffect create(Object[] args) {
            final OnTag e = new OnTag();
            e.tag = ActionTag.valueOf(((String) args[0]).toLowerCase());
            e.effect = (Effect) args[1];
            return e;
        }
    }
}
