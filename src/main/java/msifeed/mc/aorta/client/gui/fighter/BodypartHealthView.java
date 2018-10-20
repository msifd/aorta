package msifeed.mc.aorta.client.gui.fighter;

import msifeed.mc.aorta.core.character.BodyPart;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.status.BodyPartHealth;
import msifeed.mc.aorta.core.status.BodyShield;
import msifeed.mc.aorta.core.status.CharStatus;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.FlatButtonLabel;
import msifeed.mc.mellow.widgets.droplist.DropList;
import msifeed.mc.mellow.widgets.input.TextInput;

import java.util.Arrays;

class BodypartHealthView extends Widget {
    private final Character character;
    private final CharStatus charStatus;

    private Widget bodypartList = new Widget();

    BodypartHealthView(Character character, CharStatus charStatus) {
        this.character = character;
        this.charStatus = charStatus;
        setLayout(ListLayout.VERTICAL);

        bodypartList.setLayout(ListLayout.VERTICAL);
        addChild(bodypartList);
        refill();
    }

    private void refill() {
        bodypartList.clearChildren();

        if (character.bodyParts.isEmpty()) {
            bodypartList.addChild(new Label("No bodyparts! Ha-Ha!"));
            return;
        }

        for (BodyPart bp : character.bodyParts.values()) {
            final BodyPartHealth bph = charStatus.health.getOrDefault(bp.name, new BodyPartHealth(bp.max, (short) 0));

            final FlatButtonLabel b = new FlatButtonLabel();
            b.setLabel(String.format("\"%s\" - %d/%d + %d", bp.name, bph.health, bp.max, bph.armor));
            b.setClickCallback(() -> getTopParent().addChild(new BodypartHealthDialog(bp, bph, h -> {
                charStatus.health.put(bp.name, h);
                refill();
            })));
            bodypartList.addChild(b);
        }

        bodypartList.addChild(new Separator());

        final Widget shield = new Widget();
        shield.setLayout(new GridLayout());

        shield.addChild(new Label("Shield type"));
        final DropList<BodyShield.Type> shieldType = new DropList<>(Arrays.asList(BodyShield.Type.values()));
        shieldType.selectItem(charStatus.shield.type.ordinal());
        shieldType.setSelectCallback(type -> charStatus.shield.type = type);
        shield.addChild(shieldType);

        shield.addChild(new Label("Shield power"));
        final TextInput shieldPower = new TextInput();
        shieldPower.getSizeHint().x = 30;
        shieldPower.setText(String.valueOf(charStatus.shield.power));
        shieldPower.setFilter(BodypartHealthView::shieldPowerFilter);
        shieldPower.setCallback(s -> charStatus.shield.power = (short) shieldPower.getInt());
        shield.addChild(shieldPower);

        bodypartList.addChild(shield);
    }

    private static boolean shieldPowerFilter(String s) {
        return s.length() < 5 && TextInput.isSignedDigit(s);
    }
}
