package msifeed.mc.commons.traits;

import cpw.mods.fml.common.FMLCommonHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public final class TraitDecoder {
    private static final HashMap<Integer, Trait> reverseCodes = new HashMap<>();

    static {
        for (Trait t : Trait.values())
            TraitDecoder.registerTrait(t);
    }

    public static Trait decode(int code) {
        return reverseCodes.get(code);
    }

    public static Set<Trait> decode(int[] codes) {
        return Arrays.stream(codes).mapToObj(TraitDecoder::decode).collect(Collectors.toSet());
    }

    private static void registerTrait(Trait trait) {
        final Trait collisionTrait = reverseCodes.get(trait.code);
        if (collisionTrait != null) {
            final String error = String.format("Trait code collision! Old:%s New:%s", collisionTrait, trait);
            FMLCommonHandler.instance().raiseException(new Exception(), error, true);
        }

        reverseCodes.put(trait.code, trait);
    }
}
