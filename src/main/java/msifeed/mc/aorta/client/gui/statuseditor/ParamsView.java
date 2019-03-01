package msifeed.mc.aorta.client.gui.statuseditor;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.status.CharStatus;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.input.TextInput;

class ParamsView extends Widget {
    ParamsView(Character character, CharStatus charStatus) {
        setLayout(new GridLayout());

        final int vitalityThreshold = character.countVitalityThreshold();
        final int vitality = charStatus.countVitality(vitalityThreshold);
        final int vitalityPercents = Math.floorDiv(vitality * 100, vitalityThreshold);
        final String overallStatus;
        if (vitalityPercents <= 0) {
            overallStatus = "dead";
        } else if (vitalityPercents <= 25) {
            overallStatus = "dying";
        } else if (vitalityPercents <= 50) {
            overallStatus = "injured";
        } else if (vitalityPercents <= 75) {
            overallStatus = "wounded";
        } else {
            overallStatus = "fine";
        }

        addChild(new Label(L10n.tr("aorta.gui.status.overall")));
        addChild(new Label(L10n.tr("aorta.status." + overallStatus)));

        addChild(new Label(L10n.tr("aorta.gui.status.vitality")));
        addChild(new Label(String.format("%d/%d (%d%%)", vitality, vitalityThreshold, vitalityPercents)));

        addChild(new Label(L10n.tr("aorta.gui.status.sanity")));
        final TextInput sanityInput = new TextInput();
        sanityInput.setText(String.valueOf(charStatus.sanity));
        sanityInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, 125));
        sanityInput.setCallback(s -> charStatus.sanity = (byte) sanityInput.getInt());
        addChild(sanityInput);

        if (character.has(Trait.psionic)) {
            addChild(new Label(L10n.tr("aorta.gui.status.psionics")));
            final TextInput psionicsInput = new TextInput();
            psionicsInput.setText(String.valueOf(charStatus.psionics));
            psionicsInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, 100));
            psionicsInput.setCallback(s -> charStatus.psionics = (byte) psionicsInput.getInt());
            addChild(psionicsInput);
        }
    }
}
