package msifeed.mc.aorta.chat;

import msifeed.mc.aorta.chat.obfuscation.*;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.utils.L10n;

public enum Language {
    VANILLA(Trait.lang_vanilla, new VanillaObfuscator()),
    COMMON(Trait.lang_common, new CommonObfuscator()),
    MENALA(Trait.lang_menala, new MenalaObfuscator()),
    GURHK(Trait.lang_gurhk, new GurhkObfuscator()),
    UMALLAN(Trait.lang_umallan, new UmallanObfuscator()),
    TERVILIAN(Trait.lang_tervilian, new TervilianObfuscator()),
    MACHINE(Trait.lang_machine, new MachineObfuscator()),
    AISTEMIA(Trait.lang_aistemia, new AistemiaObfuscator()),
    FORGOTTEN(Trait.lang_forgotten, new ForgottenObfuscator()),
    ENLIMIAN(Trait.lang_enlimian, new EnlimianObfuscator()),
    TRANSCRIPTOR(Trait.lang_transcriptor, new TranscriptorObfuscator()),
    UNDERWATER(Trait.lang_underwater, new UnderwaterObfuscator()),
    KSHEMIN(Trait.lang_kshemin, new KsheminObfuscator()),
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
        return L10n.tr("aorta.lang." + name().toLowerCase());
    }

    public String shortTr() {
        return L10n.tr("aorta.lang." + name().toLowerCase() + ".short");
    }
}
