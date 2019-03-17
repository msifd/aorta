package msifeed.mc.aorta.client.gui.status;

import msifeed.mc.aorta.core.character.BodyShield;
import msifeed.mc.aorta.core.character.CharStatus;
import msifeed.mc.aorta.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.droplist.DropList;
import msifeed.mc.mellow.widgets.input.TextInput;

import java.util.Arrays;

class ShieldView extends Widget {
    private final CharStatus status;
    private final boolean editable;

    ShieldView(CharStatus status, boolean editable) {
        this.status = status;
        this.editable = editable;
        refill();
    }

    @Override
    public void refill() {
        clearChildren();
        if (editable)
            fillEditable();
        else
            fillNonEditable();
    }

    private void fillEditable() {
        setLayout(new GridLayout());

        addChild(new Label(L10n.tr("aorta.gui.status.shield_type")));
        final DropList<BodyShield.Type> shieldType = new DropList<>(Arrays.asList(BodyShield.Type.values()));
        shieldType.selectItem(status.shield.type.ordinal());
        shieldType.setSelectCallback(type -> status.shield.type = type);
        addChild(shieldType);

        addChild(new Label(L10n.tr("aorta.gui.status.shield_power")));
        final TextInput shieldPower = new TextInput();
        shieldPower.getSizeHint().x = 30;
        shieldPower.setText(String.valueOf(status.shield.power));
        shieldPower.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, 100));
        shieldPower.setCallback(s -> status.shield.power = (short) shieldPower.getInt());
        addChild(shieldPower);
    }

    private void fillNonEditable() {
        setLayout(new GridLayout(5));

        addChild(new Label(L10n.tr("aorta.gui.status.shield_type")));
        addChild(new Label(status.shield.type.toString()));

        addChild(new Label(L10n.tr("aorta.gui.status.shield_power")));
        addChild(new Label(String.valueOf(status.shield.power)));
    }
}
