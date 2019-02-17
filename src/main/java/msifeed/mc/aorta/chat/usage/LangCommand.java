package msifeed.mc.aorta.chat.usage;

import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.commands.ExtCommand;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        LangAttribute.get(entity).ifPresent(language -> {
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
        return CharacterAttribute.get(entity)
                .map(c -> Stream.of(Language.values())
                        .filter(l -> c.traits.contains(l.trait))
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }
}
