package msifeed.mc.extensions.noclip;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

public enum NoclipRenderHandler {
    INSTANCE;

    public static void init() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    @SubscribeEvent
    public void onRenderEvent(RenderBlockOverlayEvent event) {
        if (event.player.noClip && event.overlayType == RenderBlockOverlayEvent.OverlayType.BLOCK)
            event.setCanceled(true);
    }
}
