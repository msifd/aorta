package msifeed.mc.aorta.genesis;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum GenesisTrait {
    // Types
    block, item,

    // Blocks
    wooden, stone, metal,
    unbreakable,
    rotatable,
    add_slabs, add_stairs;

    public static final ImmutableList<String> strings = ImmutableList.copyOf(Arrays.stream(GenesisTrait.values())
            .map(Object::toString)
            .collect(Collectors.toList()));
}
