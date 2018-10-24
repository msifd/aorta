package msifeed.mc.aorta.core.character;

import com.google.common.collect.ImmutableList;
import msifeed.mc.aorta.core.rules.Dices;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Grade {
    BAD, NORMAL, GOOD, GREAT, EXCELLENT;

    public static final ImmutableList<String> STRINGS = ImmutableList.copyOf(Arrays.stream(Grade.values())
            .map(Object::toString)
            .collect(Collectors.toList()));

    public int value() {
        return ordinal() + 1;
    }

    public int roll() {
        return Dices.feature(value());
    }

    @Override
    public String toString() {
        switch (this) {
            case BAD:
                return "Bad";
            case NORMAL:
                return "Normal";
            case GOOD:
                return "Good";
            case GREAT:
                return "Great";
            case EXCELLENT:
                return "Excellent";
            default:
                return super.toString();
        }
    }
}
