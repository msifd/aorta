package msifeed.mc.aorta.client.gui.status;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.sys.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;

class OtherView extends Widget {
    private final Character character;
    private final boolean editable;

    OtherView(Character character, boolean editable) {
        this.character = character;
        this.editable = editable;

        setLayout(new GridLayout());
        refill();
    }

    @Override
    public void refill() {
        clearChildren();
        if (editable)
            fillEditable();
        else
            fillNonEditable();
    }

    private void fillEditable() {
        clearChildren();

        addChild(new Label(L10n.tr("aorta.gui.status.other.name")));
        final TextInput nameInput = new TextInput();
        nameInput.getSizeHint().x = 100;
        nameInput.setText(String.valueOf(character.name));
        nameInput.setFilter(TextInput::isValidName);
        nameInput.setCallback(s -> character.name = s);
        addChild(nameInput);

        addChild(new Label(L10n.tr("aorta.gui.status.other.wiki")));
        final TextInput wikiInput = new TextInput();
        wikiInput.getSizeHint().x = 100;
        wikiInput.setText(String.valueOf(character.wikiPage));
        wikiInput.setFilter(s -> s.length() < 256);
        wikiInput.setCallback(s -> character.wikiPage = s);
        addChild(wikiInput);
    }

    private void fillNonEditable() {
        clearChildren();

        if (!character.wikiPage.isEmpty()) {
            addChild(new Label(L10n.tr("aorta.gui.status.other.wiki")));
            addChild(new WikiUrlLabel(character.wikiPage));
        }
    }
}
