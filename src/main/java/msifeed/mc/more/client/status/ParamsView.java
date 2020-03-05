package msifeed.mc.more.client.status;

import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.sys.utils.L10n;

class ParamsView extends Widget {
    private final Character character;
    private final boolean editable;

    ParamsView(Character character, boolean editable) {
        this.character = character;
        this.editable = editable;
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
        setLayout(new GridLayout());

        addChild(new Label(L10n.tr("more.gui.status.estitence")));
        addChild(new Label(Integer.toString(character.estitence)));

        final int sin = character.sin;
        final String sinLevel = L10n.tr("more.status.sin." +
                (sin < 0 ? "-1" : sin > 0 ? "1" : "0"));

        addChild(new Label(L10n.tr("more.gui.status.sin")));
        addChild(new Label(sinLevel + (sin > 0 ? " (" + sin + ")" : "")));
    }

    private void fillNonEditable() {
        setLayout(new GridLayout(2));
    }
}
