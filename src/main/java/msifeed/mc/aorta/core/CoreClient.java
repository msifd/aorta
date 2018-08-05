package msifeed.mc.aorta.core;

import msifeed.mc.aorta.chat.net.SpeechMessageHandler;
import msifeed.mc.aorta.core.client.DebugHud;
import msifeed.mc.aorta.core.client.DisableVanillaHud;
import msifeed.mc.aorta.core.meta.ItemMeta;
import net.minecraftforge.common.MinecraftForge;

public class CoreClient extends Core {
    private ItemMeta itemMeta = new ItemMeta();

    @Override
    public void init() {
        super.init();

        itemMeta.init();

        MinecraftForge.EVENT_BUS.register(new SpeechMessageHandler());
        MinecraftForge.EVENT_BUS.register(DebugHud.INSTANCE);
        MinecraftForge.EVENT_BUS.register(DisableVanillaHud.INSTANCE);
    }
}
