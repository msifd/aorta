package msifeed.mc.aorta.client.gui.chareditor;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.input.TextInput;

class EditParamsView extends Widget {
    EditParamsView(Character character) {
        setLayout(new GridLayout());

        addChild(new Label(L10n.tr("aorta.gui.status.vitality")));
        final TextInput sanityInput = new TextInput();
        sanityInput.getSizeHint().x = 20;
        sanityInput.setText(String.valueOf(character.vitalityRate));
        sanityInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 1, 100));
        sanityInput.setCallback(s -> character.vitalityRate = (byte) sanityInput.getInt());
        addChild(sanityInput);
    }
}
