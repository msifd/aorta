package msifeed.mc.aorta.chat;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.attributes.AttributeHandler;
import msifeed.mc.aorta.chat.net.SpeechMessage;
import msifeed.mc.aorta.chat.net.SpeechMessageHandler;
import msifeed.mc.aorta.chat.selection.LangAttribute;
import msifeed.mc.aorta.chat.selection.LangCommand;
import msifeed.mc.aorta.chat.selection.OfftopCommand;
import net.minecraft.command.CommandHandler;
import net.minecraftforge.common.MinecraftForge;

public class Speechat {
    static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Aorta.MODID + ".chat");

    public void init() {
        CHANNEL.registerMessage(SpeechMessageHandler.class, SpeechMessage.class, 0x01, Side.CLIENT);
        MinecraftForge.EVENT_BUS.register(new ChatHandler());
        AttributeHandler.INSTANCE.registerAttribute(LangAttribute.INSTANCE);
    }

    public void registerCommands(CommandHandler handler) {
        handler.registerCommand(new LangCommand());
        handler.registerCommand(new OfftopCommand());
    }
}
