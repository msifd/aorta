package msifeed.mc.aorta.chat;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.chat.commands.*;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.chat.net.SpeechMessageHandler;
import msifeed.mc.aorta.core.utils.LangAttribute;
import msifeed.mc.aorta.sys.attributes.AttributeHandler;
import net.minecraft.command.CommandHandler;
import net.minecraftforge.common.MinecraftForge;

public enum Speechat {
    INSTANCE;

    static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Aorta.MODID + ".chat");

    public static void init() {
        CHANNEL.registerMessage(SpeechMessageHandler.class, ChatMessage.class, 0x01, Side.CLIENT);
        MinecraftForge.EVENT_BUS.register(new ChatHandler());
        AttributeHandler.INSTANCE.registerAttribute(LangAttribute.INSTANCE);
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
