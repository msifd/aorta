package msifeed.mc.aorta.client.gui.status;

import msifeed.mc.aorta.core.character.CharStatus;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.input.TextInput;

class ParamsView extends Widget {
    private final Character character;
    private final CharStatus status;
    private final boolean editable;

    ParamsView(Character character, CharStatus status, boolean editable) {
        this.character = character;
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

        final int vitalityThreshold = character.countVitalityThreshold();
        final int vitality = status.countVitality(vitalityThreshold);
        final int vitalityLevel = status.vitalityLevel(vitality, vitalityThreshold);
        final String vitalityLevelStr = L10n.tr("aorta.status.vitality." + vitalityLevel);

        addChild(new Label(L10n.tr("aorta.gui.status.vitality")));
        addChild(new Label(String.format("%d/%d (%s)", vitality, vitalityThreshold, vitalityLevelStr)));

        addChild(new Label(L10n.tr("aorta.gui.status.sanity")));

        final TextInput sanityInput = new TextInput();
        sanityInput.getSizeHint().x = 25;
        sanityInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 1, 125));
        sanityInput.setCallback(s -> status.sanity = (byte) sanityInput.getInt());
        sanityInput.setText(String.valueOf(status.sanity));
        addChild(sanityInput);

        if (character.has(Trait.psionic)) {
            addChild(new Label(L10n.tr("aorta.gui.status.psionics")));

            final Widget psiRight = new Widget();
            psiRight.setLayout(ListLayout.HORIZONTAL);
            addChild(psiRight);

            final Label psiDesc = new Label();
            psiDesc.getPos().y = 2;

            final TextInput psionicsInput = new TextInput();
            psionicsInput.getSizeHint().x = 25;
            psionicsInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, character.psionics));
            psionicsInput.setCallback(s -> {
                status.psionics = (byte) psionicsInput.getInt();
                final int psiPercent = character.psionics > 0 ? Math.floorDiv(status.psionics * 100, character.psionics) : 0;
                psiDesc.setText(String.format("/%d (%d%%)", character.psionics, psiPercent));
            });
            psionicsInput.setText(String.valueOf(status.psionics));

            psiRight.addChild(psionicsInput);
            psiRight.addChild(psiDesc);
        }
    }

    private void fillNonEditable() {
        setLayout(new GridLayout(5));

        final int vitalityThreshold = character.countVitalityThreshold();
        final int vitality = status.countVitality(vitalityThreshold);
        final int vitalityLevel = status.vitalityLevel(vitality, vitalityThreshold);
        addChild(new Label(L10n.tr("aorta.gui.status.vitality")));
        addChild(new Label(String.format("%d/%d (%s)",
                vitality,
                vitalityThreshold,
                L10n.tr("aorta.status.vitality." + vitalityLevel))));

        if (character.has(Trait.psionic)) {
            final int psionicsLevel = status.psionicsLevel(character);
            addChild(new Label(L10n.tr("aorta.gui.status.psionics")));
            addChild(new Label(String.format("%d (%s)",
                    status.psionics,
                    L10n.tr("aorta.status.psionics." + psionicsLevel))));
        }
    }
}
