package msifeed.mc.aorta.client.gui.status;

import msifeed.mc.aorta.core.character.BodyPart;
import msifeed.mc.aorta.core.character.BodyPartHealth;
import msifeed.mc.aorta.sys.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.input.TextInput;
import msifeed.mc.mellow.widgets.window.Window;

import java.util.function.Consumer;

class BodypartHealthDialog extends Window {
    private final BodyPart bodypart;
    private final BodyPartHealth health;

    private final ButtonLabel doneBtn = new ButtonLabel();

    BodypartHealthDialog(BodyPart _bodypart, BodyPartHealth _health, Consumer<BodyPartHealth> consumer) {
        this.bodypart = new BodyPart(_bodypart);
        this.health = new BodyPartHealth(_health);
        setTitle("Edit health");
        setZLevel(5);
        setFocused(this);

        final Widget content = getContent();
        content.setLayout(ListLayout.VERTICAL);

        final Widget partInfo = new Widget();
        partInfo.setLayout(new GridLayout());
        partInfo.addChild(new Label(bodypart.name));
        partInfo.addChild(new Label(bodypart.type.toString()));
        partInfo.addChild(new Label("Max health"));
        partInfo.addChild(new Label(bodypart.max + (bodypart.fatal ? " " + L10n.tr("aorta.gui.status.fatal") : "")));
        content.addChild(partInfo);

        content.addChild(new Separator());

        final Widget params = new Widget();
        params.setLayout(new GridLayout());
        content.addChild(params);

        params.addChild(new Label("Health"));
        final TextInput healthInput = new TextInput();
        healthInput.getSizeHint().x = 30;
        healthInput.setText(String.valueOf(health.health));
        healthInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, bodypart.max));
        healthInput.setCallback(s -> health.health = (short) healthInput.getInt());
        params.addChild(healthInput);

        params.addChild(new Label("Armor"));
        final TextInput armorInput = new TextInput();
        armorInput.getSizeHint().x = 30;
        armorInput.setText(String.valueOf(health.armor));
        armorInput.setFilter(BodypartHealthDialog::healthFilter);
        armorInput.setCallback(s -> health.armor = (short) armorInput.getInt());
        params.addChild(armorInput);

        content.addChild(new Separator());

        final Widget footer = new Widget();
        footer.setLayout(ListLayout.HORIZONTAL);
        content.addChild(footer);

        doneBtn.setLabel("Apply");
        doneBtn.getMargin().left = doneBtn.getMargin().right = 10;
        doneBtn.setClickCallback(() -> {
            if (isHealthInvalid())
                return;
            getParent().removeChild(this);
            consumer.accept(health);
        });
        footer.addChild(doneBtn);

        final ButtonLabel cancelBtn = new ButtonLabel("Cancel");
        cancelBtn.setClickCallback(() -> getParent().removeChild(this));
        footer.addChild(cancelBtn);
    }

    @Override
    protected void updateSelf() {
        doneBtn.setDisabled(isHealthInvalid());
    }

    private boolean isHealthInvalid() {
        return health.health < 0
                || health.armor < 0
                || health.health > bodypart.max;
    }

    private static boolean healthFilter(String s) {
        return s.length() < 5 && TextInput.isUnsignedInt(s);
    }
}
