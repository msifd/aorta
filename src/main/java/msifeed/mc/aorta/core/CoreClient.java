package msifeed.mc.aorta.core;

import msifeed.mc.aorta.core.chat.ChatHandlerClient;
import msifeed.mc.aorta.core.client.DebugHud;
import msifeed.mc.aorta.core.client.DisableVanillaHud;
import net.minecraftforge.common.MinecraftForge;

public class CoreClient extends Core {
    @Override
    public void init() {
        super.init();

        MinecraftForge.EVENT_BUS.register(new ChatHandlerClient());

        MinecraftForge.EVENT_BUS.register(DebugHud.INSTANCE);
        MinecraftForge.EVENT_BUS.register(DisableVanillaHud.INSTANCE);
    }
}
