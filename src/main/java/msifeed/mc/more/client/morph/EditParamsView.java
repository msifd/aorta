package msifeed.mc.more.client.morph;

import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;
import msifeed.mc.more.crabs.character.Character;

class EditParamsView extends Widget {
    EditParamsView(Character character) {
        setLayout(new GridLayout());

        addChild(new Label("Fists damage"));
        final TextInput fistsDmgInput = new TextInput();
        fistsDmgInput.getSizeHint().x = 30;
        fistsDmgInput.setText(String.valueOf(character.fistsDamage));
        fistsDmgInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, 100));
        fistsDmgInput.setCallback(s -> character.fistsDamage = fistsDmgInput.getInt());
        addChild(fistsDmgInput);

        addChild(new Label("Estitence"));
        final TextInput estitenceInput = new TextInput();
        estitenceInput.getSizeHint().x = 30;
        estitenceInput.setText(String.valueOf(character.estitence));
        estitenceInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 1, 100));
        estitenceInput.setCallback(s -> character.estitence = estitenceInput.getInt());
        addChild(estitenceInput);

        addChild(new Label("Sin"));
        final TextInput sinInput = new TextInput();
        sinInput.getSizeHint().x = 30;
        sinInput.setText(String.valueOf(character.sin));
        sinInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, -1, 100));
        sinInput.setCallback(s -> character.sin = sinInput.getInt());
        addChild(sinInput);
    }
}
