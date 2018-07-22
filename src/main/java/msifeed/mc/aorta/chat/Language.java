package msifeed.mc.aorta.chat;

import msifeed.mc.aorta.chat.obfuscation.LangObfuscator;
import msifeed.mc.aorta.chat.obfuscation.MenalaObfuscator;
import msifeed.mc.aorta.core.traits.Trait;

public enum Language {
    VANILLA(null, null), MENALA(Trait.lang_menala, new MenalaObfuscator());

    public Trait trait;
    public LangObfuscator obfuscator;

    Language(Trait trait, LangObfuscator obfuscator) {
        this.trait = trait;
        this.obfuscator = obfuscator;
    }
}
