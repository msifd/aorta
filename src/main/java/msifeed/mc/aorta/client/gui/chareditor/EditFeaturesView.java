package msifeed.mc.aorta.client.gui.chareditor;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;

import java.util.Map;

class EditFeaturesView extends Widget {
    EditFeaturesView(Character character) {
        setLayout(new GridLayout());

        for (Map.Entry<Feature, Integer> entry : character.features.entrySet()) {
            addChild(new Label(entry.getKey().toString() + ':'));
            final TextInput input = new TextInput();
            input.getSizeHint().x = 29;
            input.setText(Integer.toString(entry.getValue()));
            input.setFilter(s -> TextInput.isUnsignedIntBetween(s, 1, 10));
            input.setCallback(s -> character.features.put(entry.getKey(), s.isEmpty() ? 0 : Integer.parseInt(s)));
            addChild(input);
        }
    }
}
