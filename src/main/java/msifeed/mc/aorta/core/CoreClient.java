package msifeed.mc.aorta.core;

import msifeed.mc.aorta.chat.net.SpeechMessageHandler;
import msifeed.mc.aorta.genesis.meta.ItemMeta;
import net.minecraftforge.common.MinecraftForge;

public class CoreClient extends Core {
    private ItemMeta itemMeta = new ItemMeta();

    @Override
    public void init() {
        super.init();
        itemMeta.init();
        MinecraftForge.EVENT_BUS.register(new SpeechMessageHandler());
    }
}
