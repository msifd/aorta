package msifeed.mc.aorta.client.gui.statuseditor;

import msifeed.mc.aorta.core.status.BodyShield;
import msifeed.mc.aorta.core.status.CharStatus;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.droplist.DropList;
import msifeed.mc.mellow.widgets.input.TextInput;

import java.util.Arrays;

class ShieldView extends Widget {
    ShieldView(CharStatus charStatus) {
        setLayout(new GridLayout());

        addChild(new Label("Shield type"));
        final DropList<BodyShield.Type> shieldType = new DropList<>(Arrays.asList(BodyShield.Type.values()));
        shieldType.selectItem(charStatus.shield.type.ordinal());
        shieldType.setSelectCallback(type -> charStatus.shield.type = type);
        addChild(shieldType);

        addChild(new Label("Shield power"));
        final TextInput shieldPower = new TextInput();
        shieldPower.getSizeHint().x = 30;
        shieldPower.setText(String.valueOf(charStatus.shield.power));
        shieldPower.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, 5));
        shieldPower.setCallback(s -> charStatus.shield.power = (short) shieldPower.getInt());
        addChild(shieldPower);
    }
}
