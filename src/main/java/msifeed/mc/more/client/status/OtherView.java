package msifeed.mc.more.client.status;

import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.sys.utils.L10n;

class OtherView extends Widget {
    private final Character character;
    private final boolean editable;

    OtherView(Character character, boolean editable) {
        this.character = character;
        this.editable = editable;

        setLayout(new GridLayout());
        refill();
    }

    public void refill() {
        clearChildren();
        if (editable)
            fillEditable();
        else
            fillNonEditable();
    }

    private void fillEditable() {
        clearChildren();

        addChild(new Label(L10n.tr("more.gui.status.other.name")));
        final TextInput nameInput = new TextInput();
//        nameInput.getSizeHint().x = 300;
        nameInput.getSizeHint().x = 100;
        nameInput.setMaxLineWidth(300);
        nameInput.setText(String.valueOf(character.name));
        nameInput.setFilter(TextInput::isValidName);
        nameInput.setCallback(s -> character.name = s);
        addChild(nameInput);

        addChild(new Label(L10n.tr("more.gui.status.other.wiki")));
        final TextInput wikiInput = new TextInput();
        wikiInput.setPlaceholderText("Название страницы");
        wikiInput.getSizeHint().x = 100;
        wikiInput.setMaxLineWidth(300);
        wikiInput.setText(String.valueOf(character.wikiPage));
        wikiInput.setCallback(s -> character.wikiPage = s);
        addChild(wikiInput);
    }

    private void fillNonEditable() {
        clearChildren();

        if (!character.wikiPage.isEmpty()) {
            addChild(new Label(L10n.tr("more.gui.status.other.wiki")));
            addChild(new WikiUrlLabel(character.wikiPage));
        }
    }
}
