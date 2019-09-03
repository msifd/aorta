package msifeed.mc.aorta.client.gui.status;

import msifeed.mc.aorta.core.character.BodyPartHealth;
import msifeed.mc.aorta.core.character.CharStatus;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.sys.utils.L10n;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.FlatButtonLabel;
import msifeed.mc.mellow.widgets.text.Label;

class BodypartHealthView extends Widget {
    private final Character character;
    private final CharStatus charStatus;
    private final boolean editable;

    private Widget bodypartList = new Widget();

    BodypartHealthView(Character character, CharStatus charStatus, boolean editable) {
        this.character = character;
        this.charStatus = charStatus;
        this.editable = editable;

        setLayout(ListLayout.VERTICAL);

        bodypartList.setLayout(new ListLayout(ListLayout.Direction.VERTICAL, 0));
        addChild(bodypartList);
        refill();
    }

    public void refill() {
        bodypartList.clearChildren();

        if (character.getBodyParts().isEmpty()) {
            bodypartList.addChild(new Label("No bodyparts! Ha-Ha!"));
            return;
        }

        character.getBodyParts().stream().sorted().forEach(bp -> {
            final BodyPartHealth bph = charStatus.health.getOrDefault(bp.name, new BodyPartHealth(bp.max, (short) 0));
            final String fatal = bp.fatal ? " " + L10n.tr("aorta.gui.status.fatal") : "";
            final String injure = bph.isInjured(bp) ? " " + L10n.tr("aorta.gui.status.injured") : "";

            final FlatButtonLabel b = new FlatButtonLabel();
            b.setDisabled(!editable);
            b.setLabel(String.format("%s - %d/%d + %d%s%s", bp.name, bph.health, bp.max, bph.armor, fatal, injure));
            b.setLayout(new AnchorLayout(AnchorLayout.Anchor.LEFT, AnchorLayout.Anchor.CENTER));
            if (editable) {
                b.setClickCallback(() -> getTopParent().addChild(new BodypartHealthDialog(bp, bph, h -> {
                    charStatus.health.put(bp.name, h);
                    refill();
                })));
            }
            bodypartList.addChild(b);
        });
    }
}
