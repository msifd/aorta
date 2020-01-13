package msifeed.mc.extensions.chat;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import msifeed.mc.Bootstrap;
import msifeed.mc.extensions.chat.client.SpeechMessageHandler;
import msifeed.mc.extensions.chat.commands.*;
import msifeed.mc.sys.attributes.AttributeHandler;
import net.minecraft.command.CommandHandler;
import net.minecraftforge.common.MinecraftForge;

public enum Speechat {
    INSTANCE;

    static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Bootstrap.MODID + ".chat");

    public static void init() {
        CHANNEL.registerMessage(SpeechMessageHandler.class, ChatMessage.class, 0x01, Side.CLIENT);
        MinecraftForge.EVENT_BUS.register(new ChatHandler());
        AttributeHandler.INSTANCE.registerAttribute(LangAttribute.INSTANCE);
    }

    public static void initClient() {
        MinecraftForge.EVENT_BUS.register(new SpeechMessageHandler());
    }

    public static void registerCommands(CommandHandler handler) {
        handler.registerCommand(new LangCommand());
        handler.registerCommand(new OfftopCommand());
        handler.registerCommand(new GlobalCommand());
        handler.registerCommand(new GmsayCommand());
        handler.registerCommand(new GmGlobalCommand());
        handler.registerCommand(new SusayCommand());
    }
}
