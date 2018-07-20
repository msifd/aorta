package msifeed.mc.aorta.core.traits;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public enum Trait {
    test,

    lang_common, lang_menala;

    public int code;

    Trait() {
        final int TRAIT_CODES_SEED = 1337; // Не менять или все трейты наебнутся нахуй
        final HashFunction hash = Hashing.murmur3_32(TRAIT_CODES_SEED);
        this.code = hash.hashUnencodedChars(toString()).asInt();
        System.out.println(toString() + ": " + code);
    }
}
