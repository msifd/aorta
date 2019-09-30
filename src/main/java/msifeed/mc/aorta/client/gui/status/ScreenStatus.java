package msifeed.mc.aorta.client.gui.status;

import msifeed.mc.aorta.core.character.CharRpc;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.sys.utils.L10n;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ScreenStatus extends MellowGuiScreen {
    private Character character;

    public ScreenStatus(EntityLivingBase entity, boolean editable, boolean isGm) {
        final Window window = new Window();
        window.setTitle(L10n.fmt("aorta.gui.status.title", entity.getCommandSenderName()));
        scene.addChild(window);

        final Widget content = window.getContent();

        CharacterAttribute.get(entity).ifPresent(c -> character = new Character(c));
        if (character == null) {
            content.addChild(new Label("Missing character data!"));
            return;
        }

        final TabArea tabs = new TabArea();
        final ParamsView paramsView = new ParamsView(character, editable);
        final BodypartsView bodypartsView = new BodypartsView(character, editable);
        final IllnessView illnessView = new IllnessView(character, editable, isGm);
        final OtherView otherView = new OtherView(character, editable);
        tabs.addTab(L10n.tr("aorta.gui.status.status"), paramsView);
        tabs.addTab(L10n.tr("aorta.gui.status.body"), bodypartsView);
        if (editable || character.illness.limit > 0)
            tabs.addTab(L10n.tr("aorta.gui.status.illness"), illnessView);
        tabs.addTab(L10n.tr("aorta.gui.status.other"), otherView);
        content.addChild(tabs);

        if (entity instanceof EntityPlayer && entity != Minecraft.getMinecraft().thePlayer) {
//        if (entity instanceof EntityPlayer) {
            content.addChild(new Separator());
            content.addChild(new PlayerHandView((EntityPlayer) entity));
        }

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
                    bodypartsView.refill();
                    illnessView.refill();
                    otherView.refill();
                }
            });
            content.addChild(new Separator());
            content.addChild(submitBtn);
        }
    }
}
