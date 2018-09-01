package msifeed.mc.aorta.chat.usage;

import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.traits.TraitType;
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
            error(sender, "You should be at least entity!");
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
            title(sender, "Current language: %s", language);
        });

        final Set<Language> knownLanguages = getKnownLanguages(entity);
        if (knownLanguages.isEmpty())
            error(sender, "You have no known languages, silly.");
        else {
            final Set<String> langStrings = knownLanguages.stream().map(Enum::toString).collect(Collectors.toSet());
            final String joinedLangs = joinNiceStringFromCollection(langStrings);
            title(sender, "Known languages:");
            send(sender, "  " + joinedLangs);
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
            info(sender, "Selected %s language", lang);
        } catch (Exception e) {
            error(sender, "Unknown language.");
        }
    }

    private Set<Language> getKnownLanguages(EntityLivingBase entity) {
        final Set<Trait> traits = CharacterAttribute.INSTANCE.get(entity).map(Character::traits).orElse(Collections.emptySet());
        final Set<Trait> langTraits = TraitType.LANG.filter(traits);
        return Arrays.stream(Language.values())
                .filter(language -> langTraits.contains(language.trait))
                .collect(Collectors.toSet());
    }
}
