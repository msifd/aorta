package msifeed.mc.aorta.core.client.gui;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.CharacterProperty;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.character.Grade;
import msifeed.mc.mellow.layout.VerticalLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Button;
import msifeed.mc.mellow.widgets.Label;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.Window;
import net.minecraft.entity.EntityLivingBase;

import java.util.Map;

public class ScreenCharEditor extends MellowGuiScreen {
    private final EntityLivingBase entity;

    public ScreenCharEditor(EntityLivingBase entity) {
        this.entity = entity;

        final Window window = new Window();
        window.setSizeHint(200, 200);
        window.setLayout(VerticalLayout.INSTANCE);
        window.setTitle("Char Editor");
        scene.addChild(window);

        final Label entityName = new Label("Entity: " + entity.getCommandSenderName());
        entityName.getMargin().bottom = 5;
        window.addChild(entityName);

        final CharacterProperty charProp = CharacterProperty.get(entity);
        charProp.getCharacter().ifPresent(c -> addCharacterInfo(window, c));

        final Button btn = new Button("Kill");
        btn.setSizeHint(20, 20);
        btn.setClickCallback(entity::setDead);
        window.addChild(btn);
    }

    private void addCharacterInfo(Window window, Character character) {
        for (Map.Entry<Feature, Grade> entry : character.features.entrySet()) {
            final String text = entry.getKey().toString() + ": " + entry.getValue().toString();
            final Label label = new Label(text);
            window.addChild(label);
        }
    }

}
