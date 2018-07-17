package msifeed.mc.aorta.chat;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;

public class ChatMessageParser {
    public static SpeechMessage parse(EntityPlayerMP sender, ChatComponentTranslation chatComponent) {
        SpeechMessage message = new SpeechMessage();
        message.language = Language.MENALA;
        message.radius = 15;
        message.sender = sender.getDisplayName();
        message.chatComponent = chatComponent;
        return message;
    }
}
