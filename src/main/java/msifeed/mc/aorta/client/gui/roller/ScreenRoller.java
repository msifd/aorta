package msifeed.mc.aorta.client.gui.roller;

import msifeed.mc.aorta.sys.utils.L10n;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.EntityLivingBase;

public class ScreenRoller extends MellowGuiScreen {
    private static int lastRollTab = 0;
    private static int lastOptionsTab = 0;
    static long prevRollTime = 0;

    private final TabArea rollTabs = new TabArea();
    private final TabArea optionsTabs = new TabArea();

    public ScreenRoller(EntityLivingBase entity) {
        final Window window = new Window();
        window.setTitle(L10n.fmt("aorta.gui.roller.title", entity.getCommandSenderName()));
        scene.addChild(window);

        final Widget content = window.getContent();
        content.setLayout(new ListLayout(ListLayout.Direction.HORIZONTAL, 2));

        final TargetView targetView = new TargetView();

        rollTabs.addTab(L10n.tr("aorta.gui.roller.feat"), new FeatureRollsView(entity, targetView));
        rollTabs.addTab(L10n.tr("aorta.gui.roller.fight"), new FightRollView(entity, targetView));
        content.addChild(rollTabs);

        optionsTabs.addTab(L10n.tr("aorta.gui.roller.target"), targetView);
        optionsTabs.addTab(L10n.tr("aorta.gui.roller.mods"), new ModifiersView(entity));
        content.addChild(optionsTabs);

        rollTabs.selectTab(lastRollTab);
        optionsTabs.selectTab(lastOptionsTab);
    }

    @Override
    public void closeGui() {
        lastRollTab = rollTabs.getCurrentTabIndex();
        lastOptionsTab = optionsTabs.getCurrentTabIndex();
        super.closeGui();
    }
}
