package msifeed.mc.aorta.chat.composer;

import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.net.ChatMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

class OfftopComposer extends ChatMessageComposer {
    static OfftopComposer INSTANCE = new OfftopComposer();

    @Override
    ChatMessage compose(SpeechType type, EntityPlayer player, String text) {
        final ChatMessage message = new ChatMessage();
        message.type = SpeechType.OFFTOP;
        message.language = Language.VANILLA;
        message.radius = 20;
        message.speaker = player.getDisplayName();
        message.text = text;
        return message;
    }

    @Override
    IChatComponent format(ChatMessage message) {
        final String prefix = String.format("[OFF] %s: %s", message.speaker, message.text);
        final ChatComponentText compPrefix = new ChatComponentText(prefix);
        compPrefix.getChatStyle().setColor(EnumChatFormatting.GRAY);
        return compPrefix;
    }
}
