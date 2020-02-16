package msifeed.mc.more.crabs.action.parser;

import msifeed.mc.more.crabs.action.effects.DynamicEffect;
import msifeed.mc.more.crabs.action.effects.Effect;
import msifeed.mc.more.crabs.action.effects.EffectsRegistry;

import java.util.Arrays;

public final class EffectStringParser {
    public static Effect parseEffect(String line) {
        return parseEffect(line.trim().split(":"));
    }

    private static Effect parseEffect(String[] tokens) {
        if (tokens.length == 0) throw new RuntimeException("empty effect string");
        if (tokens.length == 1) return parseSimpleEffect(tokens[0]);
        return parseDynEffect(tokens);
    }

    private static Effect parseSimpleEffect(String token) {
        final Effect effect = EffectsRegistry.getEffect(token);
        if (effect == null)
            throw new RuntimeException("unknown effect name `" + token + "`.");
        return EffectsRegistry.getEffect(token);
    }

    private static DynamicEffect parseDynEffect(String[] tokens) {
        final Effect effect = EffectsRegistry.getEffect(tokens[0]);
        if (!(effect instanceof DynamicEffect))
            throw new RuntimeException("unknown dynamic effect name `" + tokens[0] + "`.");

        final DynamicEffect de = (DynamicEffect) effect;
        final DynamicEffect.EffectArgs[] argTypes = de.args();
        if (tokens.length - 1 < argTypes.length)
            throw new RuntimeException("required " + argTypes.length + " args, provided " + (tokens.length - 1) + ".");

        final Object[] args = new Object[argTypes.length];
        for (int i = 0; i < args.length; i++) {
            switch (argTypes[i]) {
                case INT:
                    args[i] = Integer.valueOf(tokens[i + 1]);
                    break;
                case FLOAT:
                    args[i] = Float.valueOf(tokens[i + 1]);
                    break;
                case EFFECT:
                    args[i] = parseEffect(Arrays.copyOfRange(tokens, i + 1, tokens.length));
                    break;
            }
        }
        de.init(args);

        return de;
    }
}
