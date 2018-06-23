package msifeed.mc.aorta.core.client.gui;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.CharacterProperty;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.character.Grade;
import msifeed.mc.aorta.props.SyncProp;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.VerticalLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.*;
import net.minecraft.entity.EntityLivingBase;

import java.util.Arrays;
import java.util.List;
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
        window.addChild(entityName);
        window.addChild(new Separator());

        final CharacterProperty charProp = CharacterProperty.get(entity);
        charProp.getCharacter().ifPresent(c -> {
            window.addChild(makeCharStatsEditor(c));
            window.addChild(new Separator());
        });

        final Button btn = new Button("Submit");
        btn.setSizeHint(20, 20);
        btn.setClickCallback(() -> {
            SyncProp.syncServer(entity, charProp);
        });
        window.addChild(btn);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private Widget makeCharStatsEditor(Character character) {
        final Widget widget = new Widget();
        widget.setLayout(new GridLayout());

        final List<Grade> gradeList = Arrays.asList(Grade.values());
        for (Map.Entry<Feature, Grade> entry : character.features.entrySet()) {
            widget.addChild(new Label(entry.getKey().toString() + ':'));
            final DropDown<Grade> dropDown = new DropDown<>(gradeList);
            dropDown.selectItem(entry.getValue().ordinal());
            dropDown.setSelectCallback(grade -> character.features.put(entry.getKey(), grade));
            widget.addChild(dropDown);
        }
        return widget;
    }

}
