package msifeed.mc.aorta.client.gui.status;

import msifeed.mc.aorta.core.character.CharStatus;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.input.TextInput;

class NameView extends Widget {
    private final CharStatus status;

    NameView(CharStatus status) {
        this.status = status;
        refill();
    }

    @Override
    public void refill() {
        clearChildren();
        setLayout(ListLayout.VERTICAL);

        final TextInput nameInput = new TextInput();
        nameInput.getSizeHint().x = 100;
        nameInput.setText(String.valueOf(status.name));
        nameInput.setFilter(TextInput::isValidName);
        nameInput.setCallback(s -> status.name = s);
        addChild(nameInput);
    }
}
