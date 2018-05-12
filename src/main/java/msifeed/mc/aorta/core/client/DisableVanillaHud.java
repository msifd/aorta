package msifeed.mc.aorta.core.client;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public enum DisableVanillaHud {
    INSTANCE;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        switch (event.type) {
            case ARMOR:
            case HEALTH:
            case FOOD:
            case EXPERIENCE:
                event.setCanceled(true);
                break;
        }
    }
}
