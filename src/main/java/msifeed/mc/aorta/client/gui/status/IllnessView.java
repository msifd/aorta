package msifeed.mc.aorta.client.gui.status;

import msifeed.mc.aorta.core.character.CharStatus;
import msifeed.mc.aorta.sys.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.button.Checkbox;
import msifeed.mc.mellow.widgets.input.TextInput;

class IllnessView extends Widget {
    private final CharStatus status;
    private final boolean editable;
    private final boolean gmEditor;

    IllnessView(CharStatus status, boolean editable, boolean gmEditor) {
        this.status = status;
        this.editable = editable;
        this.gmEditor = gmEditor;
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

        if (status.illness.illnessThreshold > 0 && status.illness.illnessThreshold < status.illness.illness) {
            addChild(new Label(L10n.tr("aorta.gui.status.illness_win")));
            addChild(new Widget());
        }

        if (gmEditor) {
            addChild(new Label(L10n.tr("aorta.gui.status.illness_max_visible")));
            final Checkbox checkbox = new Checkbox(status.illness.thresholdVisible);
            checkbox.setCallback(b -> status.illness.thresholdVisible = b);
            addChild(checkbox);

            addChild(new Label(L10n.tr("aorta.gui.status.illness_max")));
            final TextInput illnessMaxInput = new TextInput();
            illnessMaxInput.getSizeHint().x = 25;
            illnessMaxInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, 999));
            illnessMaxInput.setCallback(s -> status.illness.illnessThreshold = illnessMaxInput.getInt());
            illnessMaxInput.setText(String.valueOf(status.illness.illnessThreshold));
            addChild(illnessMaxInput);
        } else if (status.illness.thresholdVisible) {
            addChild(new Label(L10n.tr("aorta.gui.status.illness_max")));
            addChild(new Label(String.valueOf(status.illness.illnessThreshold)));
        }

        addChild(new Label(L10n.tr("aorta.gui.status.illness")));
        final TextInput illnessInput = new TextInput();
        illnessInput.getSizeHint().x = 25;
        illnessInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, 999));
        illnessInput.setCallback(s -> status.illness.illness = illnessInput.getInt());
        illnessInput.setText(String.valueOf(status.illness.illness));
        addChild(illnessInput);


        addChild(new Label(L10n.tr("aorta.gui.status.treatment")));
        final TextInput treatmentInput = new TextInput();
        treatmentInput.getSizeHint().x = 25;
        treatmentInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, 999));
        treatmentInput.setCallback(s -> status.illness.treatment = treatmentInput.getInt());
        treatmentInput.setText(String.valueOf(status.illness.treatment));
        addChild(treatmentInput);
    }

    private void fillNonEditable() {
        setLayout(new GridLayout(5));

        if (status.illness.illnessThreshold > 0 && status.illness.illness >= status.illness.illnessThreshold) {
            addChild(new Label(L10n.tr("aorta.gui.status.illness_win")));
            addChild(new Widget());
        }

        if (gmEditor || status.illness.thresholdVisible) {
            addChild(new Label(L10n.tr("aorta.gui.status.illness_max")));
            addChild(new Label(String.valueOf(status.illness.illnessThreshold)));
        }

        addChild(new Label(L10n.tr("aorta.gui.status.illness")));
        addChild(new Label(String.valueOf(status.illness.illness)));
        addChild(new Label(L10n.tr("aorta.gui.status.treatment")));
        addChild(new Label(String.valueOf(status.illness.treatment)));
    }
}
