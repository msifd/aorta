package msifeed.mc.aorta.client.gui.chareditor;

import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.status.CharStatus;
import msifeed.mc.aorta.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.Button;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.input.TextInput;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;

public class ScreenCharEditor extends MellowGuiScreen {
    private final EntityLivingBase entity;

    private final Widget leftSection = new Widget();
    private final Widget rightSection = new Widget();
    private final Button submitBtn = new ButtonLabel("Submit");

    private Character character = null;
    private CharStatus charStatus = null;

    public ScreenCharEditor(EntityLivingBase entity) {
        this.entity = entity;

        final Window window = new Window();
        window.setTitle(L10n.tr("aorta.gui.char_editor"));
        scene.addChild(window);

        final Widget content = window.getContent();

        leftSection.setLayout(ListLayout.VERTICAL);
        rightSection.setLayout(ListLayout.VERTICAL);
        Widget mainSection = new Widget();
        mainSection.setLayout(new ListLayout(ListLayout.Direction.HORIZONTAL, 2));
        mainSection.addChild(leftSection);
        mainSection.addChild(rightSection);
        content.addChild(mainSection);

        refill();
    }

    @Override
    public void drawScreen(int xMouse, int yMouse, float tick) {
        submitBtn.setDisabled(!entity.isEntityAlive());
        super.drawScreen(xMouse, yMouse, tick);
    }

    private void refill() {
        CharacterAttribute.get(entity).ifPresent(c -> this.character = new Character(c));
        StatusAttribute.get(entity).ifPresent(s -> this.charStatus = new CharStatus(s));

        refillLeft();
        refillRight();
    }

    private void refillLeft() {
        leftSection.clearChildren();

        leftSection.addChild(new Label("Entity: " + entity.getCommandSenderName()));

        if (character == null) {
            final Button addDataBtn = new ButtonLabel("Add data");
            addDataBtn.setClickCallback(() -> {
                character = new Character();
                charStatus = new CharStatus();
                refill();
            });
            leftSection.addChild(addDataBtn);
            return;
        } else {
            addFeatures();
        }

        leftSection.addChild(new Separator());

        if (!(entity instanceof EntityPlayer))
            addClearButton(entity);

        submitBtn.setClickCallback(() -> {
            if (!entity.isEntityAlive())
                System.out.println("entity is actually dead");
            else if (character != null) {
                CharacterAttribute.INSTANCE.set(entity, character);
                StatusAttribute.INSTANCE.set(entity, charStatus);
            }
        });
        leftSection.addChild(submitBtn);
    }

    private void addFeatures() {
        final Widget features = new Widget();
        features.setLayout(new GridLayout());

        for (Map.Entry<Feature, Integer> entry : character.features.entrySet()) {
            features.addChild(new Label(entry.getKey().toString() + ':'));
            final TextInput input = new TextInput();
            input.getSizeHint().x = 29;
            input.setText(Integer.toString(entry.getValue()));
            input.setFilter(s -> TextInput.isUnsignedIntBetween(s, 1, 10));
            input.setCallback(s -> character.features.put(entry.getKey(), s.isEmpty() ? 0 : Integer.parseInt(s)));
            features.addChild(input);
        }

        leftSection.addChild(new Separator());
        leftSection.addChild(features);
    }

    private void addClearButton(EntityLivingBase entity) {
        final Button btn = new ButtonLabel("Clear all data");
        btn.setClickCallback(() -> {
            CharacterAttribute.INSTANCE.set(entity, null);
            StatusAttribute.INSTANCE.set(entity, null);
            character = null;
            charStatus = null;
            refill();
        });
        leftSection.addChild(btn);
    }

    private void refillRight() {
        rightSection.clearChildren();
        rightSection.setDirty();

        if (character != null && charStatus != null)
            rightSection.addChild(new BodypartManageView(character, charStatus));
    }
}
