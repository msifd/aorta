package msifeed.mc.aorta.chat;

import net.minecraftforge.common.MinecraftForge;

public class Speechat {
    public SpeechHandler speechHandler = new SpeechHandler();

    public void init() {
        MinecraftForge.EVENT_BUS.register(new ChatHandler());
    }
}
