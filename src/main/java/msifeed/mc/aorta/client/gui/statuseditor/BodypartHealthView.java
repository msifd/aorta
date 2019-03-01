package msifeed.mc.aorta.client.gui.statuseditor;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.status.BodyPartHealth;
import msifeed.mc.aorta.core.status.CharStatus;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.button.FlatButtonLabel;

class BodypartHealthView extends Widget {
    private final Character character;
    private final CharStatus charStatus;

    private Widget bodypartList = new Widget();

    BodypartHealthView(Character character, CharStatus charStatus) {
        this.character = character;
        this.charStatus = charStatus;
        setLayout(ListLayout.VERTICAL);

        bodypartList.setLayout(new ListLayout(ListLayout.Direction.VERTICAL, 0));
        addChild(bodypartList);
        refill();
    }

    private void refill() {
        bodypartList.clearChildren();

        if (character.bodyParts.isEmpty()) {
            bodypartList.addChild(new Label("No bodyparts! Ha-Ha!"));
            return;
        }

        character.bodyParts.values().stream().sorted().forEach(bp -> {
            final BodyPartHealth bph = charStatus.health.getOrDefault(bp.name, new BodyPartHealth(bp.max, (short) 0));

            final FlatButtonLabel b = new FlatButtonLabel();
            b.setLabel(String.format("%s - %d/%d + %d", bp.name, bph.health, bp.max, bph.armor));
            b.setLayout(new AnchorLayout(AnchorLayout.Anchor.RIGHT, AnchorLayout.Anchor.CENTER));
            b.setClickCallback(() -> getTopParent().addChild(new BodypartHealthDialog(bp, bph, h -> {
                charStatus.health.put(bp.name, h);
                refill();
            })));
            bodypartList.addChild(b);
        });
    }
}
