package msifeed.mc.more.client.combat;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.window.Window;
import msifeed.mc.more.crabs.combat.CombatContext;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.sys.attributes.AttributeUpdateEvent;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;

public class CombatScreen extends MellowGuiScreen {
    private final EntityLivingBase entity;
    private final Widget content;

    public CombatScreen(EntityLivingBase entity) {
        this.entity = entity;

        final Window window = new Window();
        window.setTitle(L10n.fmt("more.gui.combat", entity.getCommandSenderName()));
        scene.addChild(window);

        content = window.getContent();
        content.setLayout(new ListLayout(ListLayout.Direction.HORIZONTAL, 2));

        refill();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void refill() {
        content.clearChildren();

        content.addChild(new ActionsView(entity));

        final TabArea tabs = new TabArea();
        tabs.addTab(L10n.tr("more.gui.combat.progress"), new ProgressView(entity));
        tabs.addTab(L10n.tr("more.gui.combat.mods"), new ModsView(entity));
        tabs.addTab(L10n.tr("more.gui.combat.rolls"), new RollAbilityView(entity));
        content.addChild(tabs);
    }

    @Override
    public void closeGui() {
        MinecraftForge.EVENT_BUS.unregister(this);
        super.closeGui();
    }

    @SubscribeEvent
    public void onAttributeUpdate(AttributeUpdateEvent event) {
        if (event.attr instanceof CombatAttribute) {
            if (event.entity != entity && event.entity != Minecraft.getMinecraft().thePlayer)
                return;

            final boolean hasLeft = CombatAttribute.get(event.entity)
                    .map(context -> context.phase == CombatContext.Phase.LEAVE)
                    .orElse(false);
            if (hasLeft)
                closeGui();
            else
                refill();
        }
    }
}
