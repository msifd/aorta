package msifeed.mc.aorta.client.gui.status;

import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.character.CharRpc;
import msifeed.mc.aorta.core.character.CharStatus;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.utils.L10n;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.EntityLivingBase;

public class ScreenStatus extends MellowGuiScreen {
    private Character character;
    private CharStatus status;

    public ScreenStatus(EntityLivingBase entity, boolean editable) {
        final Window window = new Window();
        window.setTitle(L10n.fmt("aorta.gui.status.title", entity.getCommandSenderName()));
        scene.addChild(window);

        final Widget content = window.getContent();

        CharacterAttribute.get(entity).ifPresent(c -> character = new Character(c));
        StatusAttribute.get(entity).ifPresent(s -> status = new CharStatus(s));

        if (character == null) {
            content.addChild(new Label("Missing character data!"));
            return;
        }
        if (status == null) {
            content.addChild(new Label("Missing status data!"));
            return;
        }

        final TabArea tabs = new TabArea();
        final ParamsView paramsView = new ParamsView(character, status, editable);
        final BodypartHealthView bodypartView = new BodypartHealthView(character, status, editable);
        tabs.addTab(L10n.tr("aorta.gui.status.status"), paramsView);
        tabs.addTab(L10n.tr("aorta.gui.status.body"), bodypartView);
        if (editable)
            tabs.addTab(L10n.tr("aorta.gui.status.name"), new NameView(status));
        content.addChild(tabs);

        if (editable) {
            final ButtonLabel submitBtn = new ButtonLabel(L10n.tr("aorta.gui.apply"));
            submitBtn.setClickCallback(() -> {
                if (!entity.isEntityAlive()) {
                    System.out.println("entity is actually dead");
                    closeGui();
                } else if (status != null) {
                    StatusAttribute.INSTANCE.set(entity, status);
                    CharRpc.updateStatus(entity.getEntityId(), status);
                    paramsView.refill();
                    bodypartView.refill();
                }
            });
            content.addChild(new Separator());
            content.addChild(submitBtn);
        }
    }
}
