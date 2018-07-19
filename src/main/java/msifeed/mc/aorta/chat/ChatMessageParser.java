package msifeed.mc.aorta.chat;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class ChatMessageParser {
    public static SpeechMessage parse(EntityPlayerMP sender, ChatComponentTranslation chatComponent) {
        SpeechMessage message = new SpeechMessage();
        message.language = Language.MENALA;
        message.radius = 15;
        message.speaker = sender.getDisplayName();
        message.chatComponent = (IChatComponent) chatComponent.getFormatArgs()[1];
        return message;
    }
}
