package msifeed.mc.aorta.core;

import msifeed.mc.aorta.chat.SpeechHandler;
import msifeed.mc.aorta.core.client.DebugHud;
import msifeed.mc.aorta.core.client.DisableVanillaHud;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

public class CoreClient extends Core {
    @Override
    public void init() {
        super.init();

        MinecraftForge.EVENT_BUS.register(new SpeechHandler());

        MinecraftForge.EVENT_BUS.register(DebugHud.INSTANCE);
        MinecraftForge.EVENT_BUS.register(DisableVanillaHud.INSTANCE);

        registerCommands(ClientCommandHandler.instance);
    }
}
