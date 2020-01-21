package msifeed.mc.aorta.client.gui.roller;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.meta.MetaInfo;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.core.utils.MetaAttribute;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.window.Window;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.EntityLivingBase;

import java.util.Optional;

public class ScreenRoller extends MellowGuiScreen {
    private static int lastRollTab = 0;
    private static int lastOptionsTab = 0;
    static long prevRollTime = 0;

    private final TabArea rollTabs = new TabArea();
    private final TabArea optionsTabs = new TabArea();

    public ScreenRoller(EntityLivingBase entity) {
        final Optional<Character> charOpt = CharacterAttribute.get(entity);
        final Optional<MetaInfo> metaOpt = MetaAttribute.get(entity);
        if (!charOpt.isPresent() || !metaOpt.isPresent()) {
            closeGui();
            return;
        }

        final Window window = new Window();
        scene.addChild(window);

        final String name = charOpt.map(c -> c.name).orElse(entity.getCommandSenderName());
        window.setTitle(L10n.fmt("aorta.gui.roller.title", name));

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
        if (!scene.getChildren().isEmpty()) {
            lastRollTab = rollTabs.getCurrentTabIndex();
            lastOptionsTab = optionsTabs.getCurrentTabIndex();
        }
        super.closeGui();
    }
}
