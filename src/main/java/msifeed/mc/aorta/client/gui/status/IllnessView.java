package msifeed.mc.aorta.client.gui.status;

import msifeed.mc.aorta.core.character.CharStatus;
import msifeed.mc.aorta.sys.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.basic.Separator;
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
        setLayout(ListLayout.VERTICAL);

        final Widget info = new Widget();
        info.setLayout(ListLayout.VERTICAL);

        if (status.illness.cured())
            info.addChild(new Label(L10n.tr("aorta.gui.status.illness.cured")));
        if (status.illness.lost())
            info.addChild(new Label(L10n.tr("aorta.gui.status.illness.lost")));
        if (status.illness.debuff() != 0)
            info.addChild(new Label(L10n.fmt("aorta.gui.status.illness.debuff", status.illness.debuff())));

        if (!info.getChildren().isEmpty()) {
            addChild(info);
            addChild(new Separator());
        }

        final Widget params = new Widget();
        params.setLayout(new GridLayout());
        addChild(params);

        if (gmEditor) {
            params.addChild(new Label(L10n.tr("aorta.gui.status.illness.limit_visible")));
            final Checkbox checkbox = new Checkbox(status.illness.limitVisible);
            checkbox.setCallback(b -> status.illness.limitVisible = b);
            params.addChild(checkbox);

            params.addChild(new Label(L10n.tr("aorta.gui.status.illness.limit")));
            final TextInput limitInput = new TextInput();
            limitInput.getSizeHint().x = 25;
            limitInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, 999));
            limitInput.setCallback(s -> status.illness.limit = limitInput.getInt());
            limitInput.setText(String.valueOf(status.illness.limit));
            params.addChild(limitInput);
        } else if (status.illness.limitVisible) {
            params.addChild(new Label(L10n.tr("aorta.gui.status.illness.limit")));
            params.addChild(new Label(String.valueOf(status.illness.limit)));
        }

        params.addChild(new Label(L10n.tr("aorta.gui.status.illness")));
        final TextInput illnessInput = new TextInput();
        illnessInput.getSizeHint().x = 25;
        illnessInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, 999));
        illnessInput.setCallback(s -> status.illness.illness = illnessInput.getInt());
        illnessInput.setText(String.valueOf(status.illness.illness));
        params.addChild(illnessInput);


        params.addChild(new Label(L10n.tr("aorta.gui.status.treatment")));
        final TextInput treatmentInput = new TextInput();
        treatmentInput.getSizeHint().x = 25;
        treatmentInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, 999));
        treatmentInput.setCallback(s -> status.illness.treatment = treatmentInput.getInt());
        treatmentInput.setText(String.valueOf(status.illness.treatment));
        params.addChild(treatmentInput);
    }

    private void fillNonEditable() {
        setLayout(new GridLayout(5));

        if (status.illness.cured()) {
            addChild(new Label(L10n.tr("aorta.gui.status.illness.cured")));
            addChild(new Widget());
        }

        if (status.illness.lost()) {
            addChild(new Label(L10n.tr("aorta.gui.status.illness.lost")));
            addChild(new Widget());
        }

        if (status.illness.debuff() != 0)
            addChild(new Label(L10n.fmt("aorta.gui.status.illness.debuff", status.illness.debuff())));

        if (gmEditor || status.illness.limitVisible) {
            addChild(new Label(L10n.tr("aorta.gui.status.illness.limit")));
            addChild(new Label(String.valueOf(status.illness.limit)));
        }

        addChild(new Label(L10n.tr("aorta.gui.status.illness")));
        addChild(new Label(String.valueOf(status.illness.illness)));
        addChild(new Label(L10n.tr("aorta.gui.status.treatment")));
        addChild(new Label(String.valueOf(status.illness.treatment)));
    }
}
