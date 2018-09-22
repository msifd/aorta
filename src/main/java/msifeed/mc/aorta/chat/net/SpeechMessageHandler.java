package msifeed.mc.aorta.chat.net;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.aorta.chat.composer.ChatMessageComposer;
import net.minecraft.util.IChatComponent;

public class SpeechMessageHandler implements IMessageHandler<ChatMessage, IMessage> {
    @Override
    public IMessage onMessage(ChatMessage message, MessageContext ctx) {
        final IChatComponent chatComponent = ChatMessageComposer.formatMessage(message);
        FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(chatComponent);
        return null;
    }
}
