package msifeed.mc.aorta.client.gui.morph;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.commons.traits.Trait;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;

class EditParamsView extends Widget {
    EditParamsView(Character character) {
        setLayout(new GridLayout());

        addChild(new Label("Vitality"));
        final TextInput sanityInput = new TextInput();
        sanityInput.getSizeHint().x = 30;
        sanityInput.setText(String.valueOf(character.vitalityRate));
        sanityInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 1, 100));
        sanityInput.setCallback(s -> character.vitalityRate = (byte) sanityInput.getInt());
        addChild(sanityInput);

        if (character.has(Trait.psionic)) {
            addChild(new Label("Psionics"));
            final TextInput psionicsInput = new TextInput();
            psionicsInput.getSizeHint().x = 30;
            psionicsInput.setText(String.valueOf(character.maxPsionics));
            psionicsInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, 100));
            psionicsInput.setCallback(s -> character.maxPsionics = (byte) psionicsInput.getInt());
            addChild(psionicsInput);
        }
    }
}
