package msifeed.mc.aorta.client.gui.status;

import msifeed.mc.aorta.core.character.BodyPart;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.sys.utils.L10n;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.FlatButtonLabel;
import msifeed.mc.mellow.widgets.text.Label;

class BodypartsView extends Widget {
    private final Character character;
    private final boolean editable;

    private Widget bodypartList = new Widget();

    BodypartsView(Character character, boolean editable) {
        this.character = character;
        this.editable = editable;

        setLayout(ListLayout.VERTICAL);

        bodypartList.setLayout(new ListLayout(ListLayout.Direction.VERTICAL, 0));
        addChild(bodypartList);
        refill();
    }

    public void refill() {
        bodypartList.clearChildren();

        if (character.bodyParts.isEmpty()) {
            bodypartList.addChild(new Label("No bodyparts! Ha-Ha!"));
            return;
        }

        for (BodyPart bp : character.bodyParts.values()) {
            final String fatal = bp.vital ? " " + L10n.tr("aorta.gui.status.vital") : "";
            final String injure = bp.isInjured() ? " " + L10n.tr("aorta.gui.status.injured") : "";

            final FlatButtonLabel b = new FlatButtonLabel();
            b.setDisabled(!editable);
            b.setLabel(String.format("%s - %d/%d [%d]%s%s", bp.name, bp.health, bp.maxHealth, bp.armor, fatal, injure));
            b.setLayout(new AnchorLayout(AnchorLayout.Anchor.LEFT, AnchorLayout.Anchor.CENTER));
            if (editable) {
                b.setClickCallback(() -> getTopParent().addChild(new BodypartDialog(bp, h -> {
                    character.bodyParts.put(bp.name, bp);
                    refill();
                })));
            }
            bodypartList.addChild(b);
        }
    }
}
