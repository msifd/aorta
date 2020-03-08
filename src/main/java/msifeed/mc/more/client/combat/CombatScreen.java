package msifeed.mc.more.client.combat;

import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.window.Window;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.combat.CombatRpc;
import msifeed.mc.more.crabs.meta.MetaInfo;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Optional;

public class CombatScreen extends MellowGuiScreen {
    public CombatScreen(EntityLivingBase entity) {
        final Window window = new Window();
        window.setTitle("combat: " + entity.getCommandSenderName());
        scene.addChild(window);

        final Widget content = window.getContent();

        final TabArea tabs = new TabArea();
        content.addChild(tabs);

        tabs.addTab("Actions", new ActionsView(entity));
        tabs.addTab("Mods", new ModsView(entity));
        tabs.addTab("Stage", new StageView(entity));

        final Optional<Character> charOpt = CharacterAttribute.get(entity);
        final Optional<MetaInfo> metaOpt = MetaAttribute.get(entity);
//        final Optional<CombatContext> combatOpt = MetaAttribute.get(entity);
        if (!charOpt.isPresent() || !metaOpt.isPresent()) {
            final ButtonLabel recruitEntity = new ButtonLabel("Recruit");
            recruitEntity.setClickCallback(() -> CombatRpc.recruitEntity(entity.getEntityId()));
            content.addChild(recruitEntity);

        } else {
//            final String name = charOpt.map(c -> c.name).orElse(entity.getCommandSenderName());
//            window.setTitle(L10n.fmt("more.gui.roller.title", name));

            if (!(entity instanceof EntityPlayer)) {
                final ButtonLabel dismissEntity = new ButtonLabel("Dismiss");
                dismissEntity.setClickCallback(() -> CombatRpc.dismissEntity(entity.getEntityId()));
                content.addChild(dismissEntity);
            }
        }
    }
}
