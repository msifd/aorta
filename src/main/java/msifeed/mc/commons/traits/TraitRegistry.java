package msifeed.mc.commons.traits;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public final class TraitRegistry {
    private static final int TRAIT_HASHER_SEED = 1337; // Не менять или все трейты наебнутся нахуй
    private static final HashFunction traitHasher = Hashing.murmur3_32(TRAIT_HASHER_SEED);
    private static final HashMap<Integer, Trait> hashToTraits = new HashMap<>();

    public static Trait decode(int code) {
        return hashToTraits.get(code);
    }

    public static Set<Trait> decode(int[] codes) {
        return Arrays.stream(codes).mapToObj(TraitRegistry::decode).collect(Collectors.toSet());
    }

    static int register(Trait t) {
        int hash = traitHasher.hashUnencodedChars(t.name()).asInt();
        while (hashToTraits.containsKey(hash))
            hash++;
        hashToTraits.put(hash, t);
        return hash;
    }
}
