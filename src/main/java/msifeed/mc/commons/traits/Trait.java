package msifeed.mc.commons.traits;

import com.google.common.hash.Hashing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public enum Trait {
    __admin,
    gm
    ;

    private static final HashMap<Integer, Trait> hashToTraits = new HashMap<>();

    public final int code;
    public final TraitType type;

    Trait() {
        final int TRAIT_HASHER_SEED = 1337; // Не менять или все трейты наебнутся нахуй
        this.code = Hashing.murmur3_32(TRAIT_HASHER_SEED).hashUnencodedChars(name()).asInt();
        this.type = TraitType.resolveType(name());
    }

    public static Trait decode(int code) {
        return hashToTraits.get(code);
    }

    public static Set<Trait> decode(int[] codes) {
        return Arrays.stream(codes)
                .mapToObj(Trait::decode)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    static {
        for (Trait t : values()) {
            if (hashToTraits.containsKey(t.code))
                throw new RuntimeException("trait code conflict for " + t.name());
            hashToTraits.put(t.code, t);
        }
    }
}
