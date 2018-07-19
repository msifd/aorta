package msifeed.mc.aorta.core.traits;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Trait {
    test,

    lang_common, lang_menala;

    public int code() {
        return TraitsHolder.code(this);
    }

    public static void init() {
        TraitsHolder.addTraits(Arrays.stream(values()).collect(Collectors.toSet()));
    }
}
