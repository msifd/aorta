package msifeed.mc.aorta.chat.selection;

import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.core.props.TraitsAttribute;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.traits.TraitTypes;
import msifeed.mc.commons.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class LangCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "lang";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/lang [lang]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityLivingBase)) {
            send(sender, "You should be at least entity!");
            return;
        }

        if (args.length == 0)
            printMyLanguages(sender);
        else
            selectLanguage(sender, args[0]);
    }

    private void printMyLanguages(ICommandSender sender) {
        final EntityLivingBase entity = (EntityLivingBase) sender;

        LangAttribute.INSTANCE.get(entity).ifPresent(language -> {
            send(sender, "Current language: %s", language);
        });

        final Set<Language> knownLanguages = getKnownLanguages(entity);
        if (knownLanguages.isEmpty())
            send(sender, "You have no known languages, silly.");
        else {
            final Set<String> langStrings = knownLanguages.stream().map(Enum::toString).collect(Collectors.toSet());
            final String joinedLangs = joinNiceStringFromCollection(langStrings);
            send(sender, "Known languages: %s", joinedLangs);
        }
    }

    private void selectLanguage(ICommandSender sender, String langName) {
        final EntityLivingBase entity = (EntityLivingBase) sender;

        try {
            final Language lang = Language.valueOf(langName.toUpperCase());
            final Set<Language> knownLanguages = getKnownLanguages(entity);
            if (!knownLanguages.contains(lang))
                throw new Exception();

            LangAttribute.INSTANCE.set(entity, lang);
            send(sender, "Selected %s language", lang);
        } catch (Exception e) {
            send(sender, "Unknown language.");
        }
    }

    private Set<Language> getKnownLanguages(EntityLivingBase entity) {
        final Set<Trait> traits = TraitsAttribute.INSTANCE.get(entity).orElse(Collections.emptySet());
        final Set<Trait> langTraits = TraitTypes.LANG.filter(traits);
        return Arrays.stream(Language.values())
                .filter(language -> langTraits.contains(language.trait))
                .collect(Collectors.toSet());
    }
}
