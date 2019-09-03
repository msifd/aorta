package msifeed.mc.aorta.client.gui.status;

import msifeed.mc.aorta.core.character.CharStatus;
import msifeed.mc.aorta.sys.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;

class OtherView extends Widget {
    private final CharStatus status;
    private final boolean editable;

    OtherView(CharStatus status, boolean editable) {
        this.status = status;
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
        nameInput.setText(String.valueOf(status.name));
        nameInput.setFilter(TextInput::isValidName);
        nameInput.setCallback(s -> status.name = s);
        addChild(nameInput);

        addChild(new Label(L10n.tr("aorta.gui.status.other.wiki")));
        final TextInput wikiInput = new TextInput();
        wikiInput.getSizeHint().x = 100;
        wikiInput.setText(String.valueOf(status.wikiPage));
        wikiInput.setFilter(s -> s.length() < 256);
        wikiInput.setCallback(s -> status.wikiPage = s);
        addChild(wikiInput);
    }

    private void fillNonEditable() {
        clearChildren();

        if (!status.wikiPage.isEmpty()) {
            addChild(new Label(L10n.tr("aorta.gui.status.other.wiki")));
            addChild(new WikiUrlLabel(status.wikiPage));
        }
    }
}
