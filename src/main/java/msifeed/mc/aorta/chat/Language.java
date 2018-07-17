package msifeed.mc.aorta.chat;

import msifeed.mc.aorta.chat.obfuscation.LangObfuscator;
import msifeed.mc.aorta.chat.obfuscation.MenalaObfuscator;

public enum Language {
    MENALA(new MenalaObfuscator());

    public LangObfuscator obfuscator;

    Language(LangObfuscator obfuscator) {
        this.obfuscator = obfuscator;
    }
}
