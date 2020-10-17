package msifeed.mc.extensions.locks;

import java.util.stream.Stream;

public enum LockType {
    NONE, BUILD_IN, PADLOCK, DIGITAL, MAGICAL;

    public static Stream<LockType> locks() {
        return Stream.of(LockType.values()).skip(1);
    }
}
