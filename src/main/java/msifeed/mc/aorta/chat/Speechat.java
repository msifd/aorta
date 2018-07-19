package msifeed.mc.aorta.chat;

import cpw.mods.fml.relauncher.Side;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.network.Networking;
import net.minecraftforge.common.MinecraftForge;

public class Speechat {
    public SpeechHandler speechHandler = new SpeechHandler();

    public void init() {
        MinecraftForge.EVENT_BUS.register(new ChatHandler());
        Networking.CHANNEL.registerMessage(Aorta.SPEECHAT.speechHandler, SpeechMessage.class, 0x11, Side.CLIENT);
    }
}
