package msifeed.mc.aorta.core.client.gui;

import msifeed.mc.aorta.core.character.BodyPart;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ScreenCharEditor extends MellowGuiScreen {
    private final EntityLivingBase entity;
    private final ScrollArea mainScroll = new ScrollArea();
    private final Widget mainSection = mainScroll.getContent();
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
        window.addChild(mainScroll);

        mainSection.setLayout(VerticalLayout.INSTANCE);
        mainSection.setSizeHint(window.getSizeHint());
        refillMainSection();

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
            addFeatures(prop.character);
            addBodyParts(prop.character);
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

    private void addFeatures(Character character) {
        final Widget features = new Widget();
        features.setLayout(new GridLayout());
        features.setVerSizePolicy(SizePolicy.Policy.MAXIMUM);

        final List<Grade> gradeList = Arrays.asList(Grade.values());
        for (Map.Entry<Feature, Grade> entry : character.features.entrySet()) {
            features.addChild(new Label(entry.getKey().toString() + ':'));
            final DropDownList<Grade> dropDown = new DropDownList<>(gradeList);
            dropDown.selectItem(entry.getValue().ordinal());
            dropDown.setSelectCallback(grade -> character.features.put(entry.getKey(), grade));
            features.addChild(dropDown);
        }

        mainSection.addChild(features);
        mainSection.addChild(new Separator());
    }

    private void addBodyParts(Character character) {
        final Widget bodyParts = new Widget();
        bodyParts.setLayout(new VerticalLayout(3));

        for (BodyPart bp : character.bodyParts) {
            bodyParts.addChild(new Label(bp.toLineString()));
        }

        final Button setBodyBtn = new Button("Set body");
        setBodyBtn.setVerSizePolicy(SizePolicy.Policy.FIXED);
        setBodyBtn.setClickCallback(() -> {
            character.bodyParts.clear();
            character.bodyParts.add(new BodyPart("head", BodyPart.Type.HEAD, 25, 50, 100));
            character.bodyParts.add(new BodyPart("body", BodyPart.Type.BODY, 75, 75, 100));
            character.bodyParts.add(new BodyPart("lhand", BodyPart.Type.HAND, 35, 60, 0));
            character.bodyParts.add(new BodyPart("rhand", BodyPart.Type.HAND, 35, 60, 0));
            character.bodyParts.add(new BodyPart("lleg", BodyPart.Type.LEG, 35, 60, 0));
            character.bodyParts.add(new BodyPart("rleg", BodyPart.Type.LEG, 35, 60, 0));
        });
        bodyParts.addChild(setBodyBtn);

        mainSection.addChild(bodyParts);
        mainSection.addChild(new Separator());
    }
}
