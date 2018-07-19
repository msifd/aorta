package msifeed.mc.aorta.core.traits;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import cpw.mods.fml.common.FMLCommonHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public final class TraitsHolder {
    private static final HashMap<Integer, Trait> traits = new HashMap<>();
    private static final HashMap<Trait, Integer> codes = new HashMap<>();
    private static final int TRAIT_CODES_SEED = 1337; // Не менять или все трейты наебнутся нахуй

    public static Trait trait(int code) {
        return traits.get(code);
    }

    public static Set<Trait> allTraits() {
        return codes.keySet();
    }

    public static int code(Trait trait) {
        return codes.get(trait);
    }

    public static Set<Trait> decode(Collection<Integer> codes) {
        return codes.stream().map(TraitsHolder::trait).collect(Collectors.toSet());
    }

    static void addTraits(Set<Trait> traits) {
        final HashFunction hash = Hashing.murmur3_32(TRAIT_CODES_SEED);
        for (Trait t : traits)
            putTrait(t, hash.hashUnencodedChars(t.toString()).asInt());

    }

    private static void putTrait(Trait trait, int code) {
        final Trait collisionTrait = traits.get(code);
        if (collisionTrait != null) {
            final String error = String.format("Trait code collision! Old:%s New:%s", collisionTrait, trait);
            FMLCommonHandler.instance().raiseException(new Exception(), error, true);
        }

        traits.put(code, trait);
        codes.put(trait, code);
    }
}
