package msifeed.mc.aorta.chat;

import msifeed.mc.aorta.chat.obfuscation.*;
import msifeed.mc.aorta.core.traits.Trait;

public enum Language {
    // TODO: common language, tervilian, aistemia_lang, forgotten_lang, enlimian_lang, transcriptor_lang, underwater_lang, kshemin_lang
    VANILLA(Trait.__lang_vanilla, new VanillaObfuscator()),
    MENALA(Trait.lang_menala, new MenalaObfuscator()),
    GURHK(Trait.lang_gurhk, new GurhkObfuscator()),
    MACHINE(Trait.lang_machine, new MachineObfuscator()),
    UMALLAN(Trait.lang_umallan, new UmallanObfuscator()),
    ;

    public Trait trait;
    public LangObfuscator obfuscator;

    Language(Trait trait, LangObfuscator obfuscator) {
        this.trait = trait;
        this.obfuscator = obfuscator;
    }
}
