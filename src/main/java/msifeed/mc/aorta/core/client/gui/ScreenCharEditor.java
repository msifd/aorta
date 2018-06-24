package msifeed.mc.aorta.core.client.gui;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.props.CharacterProperty;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.character.Grade;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.VerticalLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ScreenCharEditor extends MellowGuiScreen {
    private final EntityLivingBase entity;
    private final Widget mainSection = new Widget();
    private final Button submitBtn = new Button("Submit");

    public ScreenCharEditor(EntityLivingBase entity) {
        this.entity = entity;

        final Window window = new Window();
        window.setSizeHint(200, 200);
        window.setLayout(VerticalLayout.INSTANCE);
        window.setTitle("Char Editor");
        scene.addChild(window);

        final Label entityName = new Label("Entity: " + entity.getCommandSenderName());
        window.addChild(entityName);
        window.addChild(new Separator());

        mainSection.setLayout(VerticalLayout.INSTANCE);
        mainSection.setSizeHint(200, 200);
        refillMainSection();
        window.addChild(mainSection);

        submitBtn.setVerSizePolicy(SizePolicy.Policy.MAXIMUM);
        submitBtn.setClickCallback(() -> {
            if (!entity.isEntityAlive())
                System.out.println("entity is actually dead");
            CharacterProperty.get(entity).syncServer(entity);
        });
        window.addChild(submitBtn);
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

        final CharacterProperty prop = CharacterProperty.get(entity);
        if (prop.character != null) {
            addCharFeatures(prop.character);
        } else {
            final Button addDataBtn = new Button("Add data");
            addDataBtn.setVerSizePolicy(SizePolicy.Policy.MAXIMUM);
            addDataBtn.setClickCallback(() -> {
                prop.character = new Character();
                refillMainSection();
            });
            mainSection.addChild(addDataBtn);
        }
    }

    private void addCharFeatures(Character character) {
        final Widget features = new Widget();
        features.setLayout(new GridLayout());
        features.setVerSizePolicy(SizePolicy.Policy.MAXIMUM);

        final List<Grade> gradeList = Arrays.asList(Grade.values());
        for (Map.Entry<Feature, Grade> entry : character.features.entrySet()) {
            features.addChild(new Label(entry.getKey().toString() + ':'));
            final DropDown<Grade> dropDown = new DropDown<>(gradeList);
            dropDown.selectItem(entry.getValue().ordinal());
            dropDown.setSelectCallback(grade -> character.features.put(entry.getKey(), grade));
            features.addChild(dropDown);
        }

        mainSection.addChild(features);
        mainSection.addChild(new Separator());
    }

}
