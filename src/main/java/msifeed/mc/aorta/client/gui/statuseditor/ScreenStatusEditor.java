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
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.EntityLivingBase;

public class ScreenStatusEditor extends MellowGuiScreen {
    private Character character;
    private CharStatus charStatus;

    public ScreenStatusEditor(EntityLivingBase entity) {
        final Window window = new Window();
        window.setTitle(L10n.fmt("aorta.gui.status", entity.getCommandSenderName()));
        scene.addChild(window);

        final Widget content = window.getContent();

        CharacterAttribute.get(entity).ifPresent(c -> character = new Character(c));
        StatusAttribute.get(entity).ifPresent(c -> charStatus = new CharStatus(c));

        if (character == null) {
            content.addChild(new Label("Missing character data!"));
            return;
        }
        if (charStatus == null) {
            content.addChild(new Label("Missing status data!"));
            return;
        }

        final TabArea tabs = new TabArea();
        tabs.addTab(L10n.tr("aorta.gui.status.status"), new ParamsView(character, charStatus));
        tabs.addTab(L10n.tr("aorta.gui.status.body"), new BodypartHealthView(character, charStatus));
        tabs.addTab(L10n.tr("aorta.gui.status.shield"), new ShieldView(charStatus));
        content.addChild(tabs);

        final ButtonLabel submitBtn = new ButtonLabel(L10n.tr("aorta.gui.apply"));
        submitBtn.setClickCallback(() -> {
            if (!entity.isEntityAlive()) {
                System.out.println("entity is actually dead");
                closeGui();
            } else if (charStatus != null) {
                StatusAttribute.INSTANCE.set(entity, charStatus);
            }
        });
        content.addChild(new Separator());
        content.addChild(submitBtn);
    }
}
