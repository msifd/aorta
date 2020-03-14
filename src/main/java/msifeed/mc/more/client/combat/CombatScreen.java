package msifeed.mc.more.client.combat;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.window.Window;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.sys.attributes.AttributeUpdateEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;

public class CombatScreen extends MellowGuiScreen {
    private final EntityLivingBase entity;
    private final Widget content;

    public CombatScreen(EntityLivingBase entity) {
        this.entity = entity;

        final Window window = new Window();
        window.setTitle("Combat: " + entity.getCommandSenderName());
        scene.addChild(window);

        content = window.getContent();
        content.setLayout(ListLayout.HORIZONTAL);

        refill();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void refill() {
        content.clearChildren();

        content.addChild(new ActionsView(entity));

        final TabArea tabs = new TabArea();
        tabs.addTab("Progress", new ProgressView(entity));
        tabs.addTab("Mods", new ModsView(entity));
        content.addChild(tabs);

        content.addChild(new StageView(entity));
    }

    @Override
    public void closeGui() {
        super.closeGui();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onAttributeUpdate(AttributeUpdateEvent event) {
        if (event.entity == entity && event.attr instanceof CombatAttribute)
            refill();
    }
}
