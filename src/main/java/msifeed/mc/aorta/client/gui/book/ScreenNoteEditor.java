package msifeed.mc.aorta.client.gui.book;

import msifeed.mc.aorta.books.RemoteBook;
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
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScreenNoteEditor extends MellowGuiScreen {
    private final BookView bookView;
    private Window closeDialog = null;

    public ScreenNoteEditor(EntityPlayer player) {
        final TextInputArea textArea = new TextInputArea();
        textArea.setWithBackground(false);
        textArea.setMaxLineWidth(BookView.BOOK_TEXT_WIDTH);
        textArea.setLineLimit(Integer.MAX_VALUE);

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

        final DropList<RemoteBook.Style> style = new DropList<>(Arrays.asList(RemoteBook.Style.values()));
        style.setSelectCallback(bookView::setStyle);
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
            RemoteBookRpc.publish(text, title.getText(), style.getSelectedItem(), lang.getSelectedItem());
//            textArea.getController().clear();
//            closeGui();
        });
        publishTab.addChild(publishBtn);

        // //


    }

    @Override
    public void closeGui() {
        if (closeDialog != null)
            return;

        closeDialog = new Window();
        closeDialog.setTitle("Close editor?");
        closeDialog.setZLevel(100);

        final Widget content = closeDialog.getContent();
        content.setLayout(ListLayout.HORIZONTAL);

        final ButtonLabel yesBtn = new ButtonLabel();
        yesBtn.setLabel("Yes");
        yesBtn.setClickCallback(super::closeGui);
        content.addChild(yesBtn);

        final ButtonLabel noBtn = new ButtonLabel("No!");
        noBtn.setSizeHint(40, 10);
        noBtn.setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
        noBtn.setClickCallback(() -> {
            scene.removeChild(closeDialog);
            closeDialog = null;
        });
        content.addChild(noBtn);

        scene.addChild(closeDialog);
    }
}
