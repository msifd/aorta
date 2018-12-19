package msifeed.mc.aorta.client.gui.statuseditor;

import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.status.CharStatus;
import msifeed.mc.aorta.utils.L10n;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.EntityLivingBase;

public class ScreenStatusEditor extends MellowGuiScreen {
    private Character character;
    private CharStatus charStatus;

    public ScreenStatusEditor(EntityLivingBase entity) {
        final Window window = new Window();
        window.setTitle(L10n.tr("aorta.gui.status_editor"));
        scene.addChild(window);

        final Widget windowContent = window.getContent();

        CharacterAttribute.get(entity).ifPresent(c -> character = new Character(c));
        StatusAttribute.get(entity).ifPresent(c -> charStatus = new CharStatus(c));

        if (character == null) {
            windowContent.addChild(new Label("Missing character data!"));
            return;
        }
        if (charStatus == null) {
            windowContent.addChild(new Label("Missing status data!"));
            return;
        }

        windowContent.addChild(new Label("Entity: " + entity.getCommandSenderName()));
        windowContent.addChild(new Separator());
        windowContent.addChild(new BodypartHealthView(character, charStatus));

        final ButtonLabel submitBtn = new ButtonLabel("Apply");
        submitBtn.setClickCallback(() -> {
            if (!entity.isEntityAlive())
                System.out.println("entity is actually dead");
            else if (character != null) {
//                CharacterAttribute.INSTANCE.set(entity, character);
                StatusAttribute.INSTANCE.set(entity, charStatus);
            }
        });
        windowContent.addChild(new Separator());
        windowContent.addChild(submitBtn);
    }
}
