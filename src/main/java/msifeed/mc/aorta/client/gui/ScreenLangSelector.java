package msifeed.mc.aorta.client.gui;

import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.usage.LangAttribute;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.traits.TraitType;
import msifeed.mc.aorta.utils.L10n;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.droplist.DropList;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.EntityLivingBase;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScreenLangSelector extends MellowGuiScreen {
    private Character character;

    public ScreenLangSelector(EntityLivingBase entity) {
        final Window window = new Window();
        window.setTitle(L10n.tr("aorta.gui.lang_selector"));
        scene.addChild(window);

        final Widget windowContent = window.getContent();
        windowContent.setLayout(ListLayout.VERTICAL);

        CharacterAttribute.get(entity).ifPresent(c -> character = new Character(c));
        if (character == null) {
            windowContent.addChild(new Label("Missing character data!"));
            return;
        }

        final Set<Trait> langTraits = TraitType.LANG.filter(character.traits);
        final List<Language> knownLanguages = Stream.of(Language.values())
                .filter(l -> langTraits.contains(l.trait))
                .collect(Collectors.toList());
        final Language backupLang = knownLanguages.isEmpty() ? Language.VANILLA : knownLanguages.get(0);
        if (!knownLanguages.contains(backupLang))
            knownLanguages.add(backupLang);
        final Language selected = LangAttribute.get(entity).orElse(backupLang);

        final DropList<Language> dropDown = new DropList<>(knownLanguages);
        dropDown.selectItem(knownLanguages.indexOf(selected));
        dropDown.setSelectCallback(lang -> LangAttribute.INSTANCE.set(entity, lang));
        windowContent.addChild(dropDown);
    }
}
