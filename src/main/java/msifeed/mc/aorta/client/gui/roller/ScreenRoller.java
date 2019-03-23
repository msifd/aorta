package msifeed.mc.aorta.client.gui.roller;

import msifeed.mc.aorta.sys.utils.L10n;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.EntityLivingBase;

public class ScreenRoller extends MellowGuiScreen {
    static long prevRollTime = 0;

    public ScreenRoller(EntityLivingBase entity) {
        final Window window = new Window();
        window.setTitle(L10n.fmt("aorta.gui.roller.title", entity.getCommandSenderName()));
        scene.addChild(window);

        final Widget content = window.getContent();

        final TabArea rollTabs = new TabArea();
        rollTabs.addTab(L10n.tr("aorta.gui.roller.feat"), new FeatureRollsView(entity));
        rollTabs.addTab(L10n.tr("aorta.gui.roller.fight"), new FightRollView(entity));
        rollTabs.addTab(L10n.tr("aorta.gui.roller.mods"), new ModifiersView(entity));
        content.addChild(rollTabs);
    }
}
