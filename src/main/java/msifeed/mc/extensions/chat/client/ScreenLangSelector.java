package msifeed.mc.extensions.chat.client;

import msifeed.mc.extensions.chat.LangAttribute;
import msifeed.mc.extensions.chat.Language;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.droplist.DropList;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.window.Window;
import msifeed.mc.more.crabs.character.CharRpc;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.EntityLivingBase;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScreenLangSelector extends MellowGuiScreen {
    private Character character;

    public ScreenLangSelector(EntityLivingBase entity) {
        final Window window = new Window();
        window.setTitle(L10n.tr("more.gui.lang_selector"));
        scene.addChild(window);

        final Widget windowContent = window.getContent();
        windowContent.setLayout(ListLayout.VERTICAL);

        CharacterAttribute.get(entity).ifPresent(c -> character = new Character(c));
        if (character == null) {
            windowContent.addChild(new Label("Missing character data!"));
            return;
        }

        final List<Language> knownLanguages = Stream.of(Language.values())
                .filter(l -> character.traits.contains(l.trait))
                .collect(Collectors.toList());
        if (knownLanguages.isEmpty())
            knownLanguages.add(Language.VANILLA);
        final Language selected = LangAttribute.get(entity).orElse(knownLanguages.get(0));

        final DropList<Language> dropDown = new DropList<>(knownLanguages);
        dropDown.selectItem(knownLanguages.indexOf(selected));
        dropDown.setSelectCallback(lang -> CharRpc.setLang(entity.getEntityId(), lang));
        windowContent.addChild(dropDown);
    }
}
