package msifeed.mc.more.client.status;

import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;
import msifeed.mc.more.crabs.character.Ability;
import msifeed.mc.more.crabs.character.Character;

public class EditAbilitiesView extends Widget {
    private final Character character;

    public EditAbilitiesView(Character character) {
        this.character = character;

        setLayout(new GridLayout());
        refill();
    }

    public void refill() {
        clearChildren();

        for (final Ability a : Ability.values())
            addAbilityParam(character, a);
    }

    private void addAbilityParam(Character character, Ability a) {
        final Widget pair = new Widget();
        pair.setLayout(ListLayout.HORIZONTAL);
        addChild(pair);

        final Label label = new Label(a.trShort() + ":");
        label.getSizeHint().x = 25;
        label.getPos().y = 1;
        pair.addChild(label);

        final TextInput input = new TextInput();
        input.getSizeHint().x = 16;

        final int abilityValue = character.abilities.getOrDefault(a, 0);
        input.setText(Integer.toString(abilityValue));
        input.setFilter(s -> TextInput.isSignedIntBetween(s, 1, 99));
        input.setCallback(s -> character.abilities.put(a, s.isEmpty() ? 1 : Integer.parseInt(s)));
        pair.addChild(input);
    }
}