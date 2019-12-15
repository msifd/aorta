package msifeed.mc.aorta.core.traits;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public enum Trait {
    __admin,

    gm,

    lang_vanilla,
    lang_common, lang_menala, lang_gurhk, lang_umallan, lang_tervilian, lang_machine,
    lang_aistemia, lang_forgotten, lang_enlimian, lang_transcriptor, lang_underwater, lang_kshemin,

    psionic,

    sanity_light, sanity_medium, sanity_hard, sanity_hardcore, sanity_extreme,

    ;

    public int code;
    public TraitType type;

    Trait() {
        final int TRAIT_CODES_SEED = 1337; // Не менять или все трейты наебнутся нахуй
        final HashFunction hash = Hashing.murmur3_32(TRAIT_CODES_SEED);
        this.code = hash.hashUnencodedChars(toString()).asInt();
        this.type = TraitType.resolveType(name());
    }
}
