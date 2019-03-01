package msifeed.mc.aorta.client.gui.roller;

import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.EntityLivingBase;

public class ScreenRoller extends MellowGuiScreen {
    static long prevRollTime = 0;

    public ScreenRoller(EntityLivingBase entity) {
        final Window window = new Window();
        window.setTitle(String.format("Roller: %s", entity.getCommandSenderName()));
        scene.addChild(window);

        final Widget content = window.getContent();

        final TabArea rollTabs = new TabArea();
        rollTabs.addTab("Feats", new FeatureRollsView(entity));
        rollTabs.addTab("Fight", new FightRollView(entity));
        content.addChild(rollTabs);
        content.addChild(new Separator());
        content.addChild(new ModifiersView(entity));
    }
}
