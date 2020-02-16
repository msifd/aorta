package msifeed.mc.more.client.status;

import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.window.Window;
import msifeed.mc.more.crabs.character.CharRpc;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.EntityLivingBase;

public class StatusScreen extends MellowGuiScreen {
    private Character character;

    public StatusScreen(EntityLivingBase entity, boolean editable, boolean isGm) {
        CharacterAttribute.get(entity).ifPresent(c -> character = new Character(c));
        if (character == null) {
            closeGui();
            return;
        }

        final Window window = new Window();
        window.setTitle(L10n.fmt("aorta.gui.status.title", character.name.isEmpty() ? entity.getCommandSenderName() : character.name));
        scene.addChild(window);

        final Widget content = window.getContent();

        final TabArea tabs = new TabArea();
        final ParamsView paramsView = new ParamsView(character, editable);
        final IllnessView illnessView = new IllnessView(character, editable, isGm);
        final OtherView otherView = new OtherView(character, editable);
        tabs.addTab(L10n.tr("aorta.gui.status.status"), paramsView);
        if (editable || character.illness.limit > 0)
            tabs.addTab(L10n.tr("aorta.gui.status.illness"), illnessView);
        tabs.addTab(L10n.tr("aorta.gui.status.other"), otherView);
        content.addChild(tabs);

        if (editable) {
            final ButtonLabel submitBtn = new ButtonLabel(L10n.tr("aorta.gui.apply"));
            submitBtn.setClickCallback(() -> {
                if (!entity.isEntityAlive()) {
                    System.out.println("entity is actually dead");
                    closeGui();
                } else if (character != null) {
                    CharacterAttribute.INSTANCE.set(entity, character);
                    CharRpc.updateChar(entity.getEntityId(), character);
                    paramsView.refill();
                    illnessView.refill();
                    otherView.refill();
                }
            });
            content.addChild(new Separator());
            content.addChild(submitBtn);
        }
    }
}
