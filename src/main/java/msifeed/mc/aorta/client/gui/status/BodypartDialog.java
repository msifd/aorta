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

    private final ButtonLabel doneBtn = new ButtonLabel();

    BodypartDialog(BodyPart bodypart, Consumer<BodyPart> consumer) {
        this.bodypart = new BodyPart(bodypart);

        setTitle("Edit health");
        setZLevel(5);
        setFocused(this);

        final Widget content = getContent();
        content.setLayout(ListLayout.VERTICAL);

        content.addChild(new Label(bodypart.name));

        final Widget params = new Widget();
        params.setLayout(new GridLayout());
        content.addChild(params);

        params.addChild(new Label("Max health"));
        params.addChild(new Label(bodypart.maxHealth + (bodypart.vital ? " " + L10n.tr("aorta.gui.status.vital") : "")));

        params.addChild(new Label("Health"));
        final TextInput healthInput = new TextInput();
        healthInput.getSizeHint().x = 30;
        healthInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, bodypart.maxHealth));
        healthInput.setCallback(s -> bodypart.health = (short) healthInput.getInt());
        healthInput.setText(String.valueOf(bodypart.health));
        params.addChild(healthInput);

        params.addChild(new Label("Armor"));
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

        doneBtn.setLabel("Apply");
        doneBtn.getMargin().left = doneBtn.getMargin().right = 10;
        doneBtn.setClickCallback(() -> {
            if (isBodypartInvalid())
                return;
            getParent().removeChild(this);
            consumer.accept(bodypart);
        });
        footer.addChild(doneBtn);

        final ButtonLabel cancelBtn = new ButtonLabel("Cancel");
        cancelBtn.setClickCallback(() -> getParent().removeChild(this));
        footer.addChild(cancelBtn);
    }

    @Override
    protected void updateSelf() {
        doneBtn.setDisabled(isBodypartInvalid());
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
