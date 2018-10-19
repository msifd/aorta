package msifeed.mc.aorta.client.gui.chareditor;

import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.character.Grade;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.Button;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.droplist.DropList;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ScreenCharEditor extends MellowGuiScreen {
    private final EntityLivingBase entity;
    private final Widget mainSection = new Widget();
    private final Button submitBtn = new ButtonLabel("Submit");

    private Character character = null;

    public ScreenCharEditor(EntityLivingBase entity) {
        this.entity = entity;

        final Window window = new Window();
        window.setTitle("Char Editor");
        scene.addChild(window);

        final Widget windowContent = window.getContent();

        final Label entityName = new Label("Entity: " + entity.getCommandSenderName());
        windowContent.addChild(entityName);

        mainSection.setLayout(ListLayout.VERTICAL);
        refillMainSection();
        windowContent.addChild(mainSection);

        submitBtn.setClickCallback(() -> {
            if (!entity.isEntityAlive())
                System.out.println("entity is actually dead");
            else if (character != null)
                CharacterAttribute.INSTANCE.set(entity, character);
        });

        windowContent.addChild(new Separator());
        windowContent.addChild(submitBtn);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int xMouse, int yMouse, float tick) {
        submitBtn.setDisabled(!entity.isEntityAlive());
        super.drawScreen(xMouse, yMouse, tick);
    }

    private void refillMainSection() {
        mainSection.clearChildren();

        CharacterAttribute.get(entity).ifPresent(c -> this.character = new Character(c));

        if (character != null) {
            addFeatures();
            addBodyParts();
            if (!(entity instanceof EntityPlayer))
                addClearButton(entity);
        } else {
            final Button addDataBtn = new ButtonLabel("Add data");
            addDataBtn.setClickCallback(() -> {
                character = new Character();
                refillMainSection();
            });
            mainSection.addChild(new Separator());
            mainSection.addChild(addDataBtn);
        }
    }

    private void addFeatures() {
        final Widget features = new Widget();
        features.setLayout(new GridLayout());

        final List<Grade> gradeList = Arrays.asList(Grade.values());
        for (Map.Entry<Feature, Grade> entry : character.features.entrySet()) {
            features.addChild(new Label(entry.getKey().toString() + ':'));
            final DropList<Grade> dropDown = new DropList<>(gradeList);
            dropDown.selectItem(entry.getValue().ordinal());
            dropDown.setSelectCallback(grade -> character.features.put(entry.getKey(), grade));
            features.addChild(dropDown);
        }

        mainSection.addChild(new Separator());
        mainSection.addChild(features);
    }

    private void addBodyParts() {
        mainSection.addChild(new Separator());
        mainSection.addChild(new BodypartManageView(character));
    }

    private void addClearButton(EntityLivingBase entity) {
        final Button btn = new ButtonLabel("Clear char data");
        btn.setClickCallback(() -> {
            CharacterAttribute.INSTANCE.set(entity, null);
            character = null;
            refillMainSection();
        });
        mainSection.addChild(btn);
    }
}
