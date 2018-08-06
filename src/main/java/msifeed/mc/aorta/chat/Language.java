package msifeed.mc.aorta.chat;

import msifeed.mc.aorta.chat.obfuscation.GurhkObfuscator;
import msifeed.mc.aorta.chat.obfuscation.LangObfuscator;
import msifeed.mc.aorta.chat.obfuscation.MachineObfuscator;
import msifeed.mc.aorta.chat.obfuscation.MenalaObfuscator;
import msifeed.mc.aorta.core.traits.Trait;

public enum Language {
    // TODO: common language, umallan language, tervilian language
    VANILLA(null, null),
    MENALA(Trait.lang_menala, new MenalaObfuscator()),
    GURHK(Trait.lang_gurhk, new GurhkObfuscator()),
    MACHINE(Trait.lang_machine, new MachineObfuscator());

    public Trait trait;
    public LangObfuscator obfuscator;

    Language(Trait trait, LangObfuscator obfuscator) {
        this.trait = trait;
        this.obfuscator = obfuscator;
    }
}
