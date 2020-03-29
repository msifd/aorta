package msifeed.mc.more.client.status;

import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;
import msifeed.mc.more.crabs.character.Ability;
import msifeed.mc.more.crabs.character.Character;

import java.util.Map;

public class EditAbilitiesView extends Widget {
    private final Character character;

    public EditAbilitiesView(Character character) {
        this.character = character;

        setLayout(new GridLayout());
        refill();
    }

    public void refill() {
        clearChildren();

        for (Map.Entry<Ability, Integer> entry : character.abilities.entrySet()) {
            addChild(new Label(entry.getKey().toString() + ':'));
            final TextInput input = new TextInput();
            input.getSizeHint().x = 29;
            input.setText(Integer.toString(entry.getValue()));
            input.setFilter(s -> TextInput.isUnsignedIntBetween(s, 1, 99));
            input.setCallback(s -> character.abilities.put(entry.getKey(), s.isEmpty() ? 0 : Integer.parseInt(s)));
            addChild(input);
        }
    }
}