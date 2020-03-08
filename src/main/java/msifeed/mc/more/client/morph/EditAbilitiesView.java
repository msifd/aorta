package msifeed.mc.more.client.morph;

import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;
import msifeed.mc.more.crabs.character.Ability;
import msifeed.mc.more.crabs.character.Character;

import java.util.Map;

class EditAbilitiesView extends Widget {
    EditAbilitiesView(Character character) {
        setLayout(new GridLayout());

        for (Map.Entry<Ability, Integer> entry : character.abilities.entrySet()) {
            addChild(new Label(entry.getKey().toString() + ':'));
            final TextInput input = new TextInput();
            input.getSizeHint().x = 29;
            input.setText(Integer.toString(entry.getValue()));
            input.setFilter(s -> TextInput.isUnsignedIntBetween(s, 1, 10));
            input.setCallback(s -> character.abilities.put(entry.getKey(), s.isEmpty() ? 0 : Integer.parseInt(s)));
            addChild(input);
        }
    }
}