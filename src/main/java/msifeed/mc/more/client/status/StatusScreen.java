package msifeed.mc.more.client.status;

import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.window.Window;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.meta.MetaInfo;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import net.minecraft.entity.EntityLivingBase;

import java.util.Optional;

public class StatusScreen extends MellowGuiScreen {
    public StatusScreen(EntityLivingBase entity, boolean editing, boolean gm) {
        final Optional<Character> charOpt = CharacterAttribute.get(entity);
        final Optional<MetaInfo> metaOpt = MetaAttribute.get(entity);
        if (!charOpt.isPresent() || !metaOpt.isPresent()) {
            closeGui();
            return;
        }

        final Window window = new Window();
        scene.addChild(window);

        final String name = charOpt.map(c -> c.name).orElse(entity.getCommandSenderName());
        window.setTitle("status: " + name);
//        window.setTitle(L10n.fmt("aorta.gui.roller.title", name));
    }
}
