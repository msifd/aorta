package msifeed.mc.aorta.client.gui.book;

import msifeed.mc.aorta.books.RemoteBookRpc;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.core.utils.LangAttribute;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.droplist.DropList;
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.text.TextInput;
import msifeed.mc.mellow.widgets.text.TextInputArea;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScreenNoteEditor extends MellowGuiScreen {
    private static final int TEXT_ROWS = 15;
    private static final int TEXT_COLS = 26;
    private static String textBackup = null;

    private final BookView bookView;

    public ScreenNoteEditor(EntityPlayer player) {
        final TextInputArea textArea = new TextInputArea();
        textArea.getController().setMaxColumns(TEXT_COLS);

        this.bookView = new BookView(textArea);

        final TabArea tabs = new TabArea();
        scene.addChild(tabs);

        tabs.addTab("Editor", bookView);

        //

        final Widget publishTab = new Widget();
        publishTab.setSizeHint(bookView.getSizeHint());
        publishTab.setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
        publishTab.setLayout(ListLayout.VERTICAL);
        tabs.addTab("Publish", publishTab);

        final TextInput title = new TextInput();
        publishTab.addChild(title);

        final DropList<BookParts> style = new DropList<>(Arrays.asList(BookParts.values()));
        style.setSelectCallback(bookView::setBookStyle);
        style.selectItem(BookParts.NOTE.ordinal());
        publishTab.addChild(style);

        final Character character = CharacterAttribute.require(player);
        final List<Language> knownLanguages = Stream.of(Language.values())
                .filter(l -> character.traits.contains(l.trait))
                .collect(Collectors.toList());
        final DropList<Language> lang = new DropList<>(knownLanguages);
        lang.selectItem(knownLanguages.indexOf(LangAttribute.require(player)));
        publishTab.addChild(lang);

        final ButtonLabel publishBtn = new ButtonLabel("Publish");
        publishBtn.setClickCallback(() -> {
            final String text = textArea.getText();
            if (text.length() < 3 || title.getText().length() < 3)
                return;
            RemoteBookRpc.publish(text, title.getText(), style.getSelectedItem().style, lang.getSelectedItem());
//            textArea.getController().clear();
//            closeGui();
        });
        publishTab.addChild(publishBtn);
    }

    @Override
    public void closeGui() {
        super.closeGui();
    }
}
