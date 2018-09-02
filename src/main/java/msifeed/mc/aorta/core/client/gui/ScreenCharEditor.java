package msifeed.mc.aorta.core.client.gui;

import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.character.BodyPart;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.character.Grade;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.VerticalLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.Button;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.droplist.DropList;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.EntityLivingBase;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ScreenCharEditor extends MellowGuiScreen {
    private final EntityLivingBase entity;
    //    private final ScrollArea mainScroll = new ScrollArea();
//    private final Widget mainSection = mainScroll.getContent();
    private final Widget mainSection = new Widget();
    private final Button submitBtn = new ButtonLabel("Submit");

    private Character character = null;

    public ScreenCharEditor(EntityLivingBase entity) {
        this.entity = entity;

        final Window window = new Window();
        window.setSizeHint(200, 200);
        window.setLayout(VerticalLayout.INSTANCE);
        window.setTitle("Char Editor");
        scene.addChild(window);

        final Label entityName = new Label("Entity: " + entity.getCommandSenderName());
        final Widget windowContent = window.getContent();
        windowContent.addChild(entityName);
        windowContent.addChild(new Separator());

        mainSection.setLayout(VerticalLayout.INSTANCE);
        mainSection.setSizeHint(window.getSizeHint());
        refillMainSection();
        windowContent.addChild(mainSection);

        submitBtn.setClickCallback(() -> {
            if (!entity.isEntityAlive())
                System.out.println("entity is actually dead");
            else if (character != null)
                CharacterAttribute.INSTANCE.set(entity, character);
        });
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

        CharacterAttribute.get(entity).ifPresent(c -> this.character = c);

        if (character != null) {
            addFeatures();
//            addBodyParts();
        } else {
            final Button addDataBtn = new ButtonLabel("Add data");
            addDataBtn.setClickCallback(() -> {
                character = new Character();
                refillMainSection();
            });
            mainSection.addChild(addDataBtn);
            mainSection.addChild(new Separator());
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

        mainSection.addChild(features);
        mainSection.addChild(new Separator());
    }

    private void addBodyParts() {
        final Widget bodyParts = new Widget();
        bodyParts.setLayout(new VerticalLayout(3));

        for (BodyPart bp : character.bodyParts) {
            bodyParts.addChild(new Label(bp.toLineString()));
        }

        final Button setBodyBtn = new ButtonLabel("Set body");
        setBodyBtn.setVerSizePolicy(SizePolicy.Policy.FIXED);
        setBodyBtn.setClickCallback(() -> {
            character.bodyParts.clear();
            character.bodyParts.add(new BodyPart("head", BodyPart.Type.HEAD, 25, 50, true));
            character.bodyParts.add(new BodyPart("body", BodyPart.Type.BODY, 75, 75, false));
            character.bodyParts.add(new BodyPart("lhand", BodyPart.Type.HAND, 35, 60, false));
            character.bodyParts.add(new BodyPart("rhand", BodyPart.Type.HAND, 35, 60, false));
            character.bodyParts.add(new BodyPart("lleg", BodyPart.Type.LEG, 35, 60, false));
            character.bodyParts.add(new BodyPart("rleg", BodyPart.Type.LEG, 35, 60, false));
        });
        bodyParts.addChild(setBodyBtn);

        mainSection.addChild(bodyParts);
        mainSection.addChild(new Separator());
    }
}
