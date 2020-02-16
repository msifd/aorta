package msifeed.mc.extensions.chat;

import msifeed.mc.commons.traits.Trait;
import msifeed.mc.extensions.chat.obfuscation.LangObfuscator;
import msifeed.mc.extensions.chat.obfuscation.VanillaObfuscator;
import msifeed.mc.sys.utils.L10n;

public enum Language {
    VANILLA(Trait.lang_vanilla, new VanillaObfuscator())
    ;

    public final Trait trait;
    public final LangObfuscator obfuscator;

    Language(Trait trait, LangObfuscator obfuscator) {
        this.trait = trait;
        this.obfuscator = obfuscator;
    }

    @Override
    public String toString() {
        return tr();
    }

    public String tr() {
        return L10n.tr("more.lang." + name().toLowerCase());
    }

    public String shortTr() {
        return L10n.tr("more.lang." + name().toLowerCase() + ".short");
    }
}
