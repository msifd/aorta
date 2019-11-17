package msifeed.mc.aorta.client.gui.status;

import msifeed.mc.aorta.core.character.BodyPart;
import msifeed.mc.aorta.sys.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;
import msifeed.mc.mellow.widgets.window.Window;

import java.util.function.Consumer;

class BodypartDialog extends Window {
    private final BodyPart bodypart;

    private final ButtonLabel applyBtn = new ButtonLabel();

    BodypartDialog(Widget origin, BodyPart bodypart, Consumer<BodyPart> consumer) {
        super(origin);

        this.bodypart = new BodyPart(bodypart);

        setTitle(L10n.tr("aorta.gui.status.health.title"));
        setFocused(this);

        final Widget content = getContent();
        content.setLayout(ListLayout.VERTICAL);

        content.addChild(new Label(L10n.tr("aorta.gui.status.health.bodypart") + ' ' + bodypart.name));

        final Widget params = new Widget();
        params.setLayout(new GridLayout());
        content.addChild(params);

        params.addChild(new Label(L10n.tr("aorta.gui.status.health.max_health")));
        params.addChild(new Label(bodypart.maxHealth + (bodypart.vital ? ", " + L10n.tr("aorta.gui.status.vital") : "")));

        params.addChild(new Label(L10n.tr("aorta.gui.status.health.health")));
        final TextInput healthInput = new TextInput();
        healthInput.getSizeHint().x = 30;
        healthInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, bodypart.maxHealth));
        healthInput.setCallback(s -> bodypart.health = (short) healthInput.getInt());
        healthInput.setText(String.valueOf(bodypart.health));
        params.addChild(healthInput);

        params.addChild(new Label(L10n.tr("aorta.gui.status.health.armor")));
        final TextInput armorInput = new TextInput();
        armorInput.getSizeHint().x = 30;
        armorInput.setFilter(BodypartDialog::healthFilter);
        armorInput.setCallback(s -> bodypart.armor = (short) armorInput.getInt());
        armorInput.setText(String.valueOf(bodypart.armor));
        params.addChild(armorInput);

        content.addChild(new Separator());

        final Widget footer = new Widget();
        footer.setLayout(ListLayout.HORIZONTAL);
        content.addChild(footer);

        applyBtn.setLabel(L10n.tr("aorta.gui.apply"));
        applyBtn.getMargin().left = applyBtn.getMargin().right = 10;
        applyBtn.setClickCallback(() -> {
            if (isBodypartInvalid())
                return;
            getParent().removeChild(this);
            consumer.accept(bodypart);
        });
        footer.addChild(applyBtn);

        final ButtonLabel cancelBtn = new ButtonLabel(L10n.tr("aorta.gui.cancel"));
        cancelBtn.setClickCallback(() -> getParent().removeChild(this));
        footer.addChild(cancelBtn);
    }

    @Override
    protected void updateSelf() {
        applyBtn.setDisabled(isBodypartInvalid());
    }

    private boolean isBodypartInvalid() {
        return bodypart.health < 0
                || bodypart.armor < 0
                || bodypart.health > bodypart.maxHealth;
    }

    private static boolean healthFilter(String s) {
        return s.length() < 5 && TextInput.isUnsignedInt(s);
    }
}
