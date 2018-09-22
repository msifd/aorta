package msifeed.mc.aorta.chat.composer;

import msifeed.mc.aorta.chat.net.ChatMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public abstract class ChatMessageComposer {
    public static ChatMessage makeMessage(SpeechType type, EntityPlayer player, ChatComponentTranslation comp) {
        return makeMessage(type, player, getTextFromTranslation(comp));
    }

    public static ChatMessage makeMessage(SpeechType type, EntityPlayer player, String text) {
        switch (type) {
            default:
            case SPEECH:
                return SpeechComposer.INSTANCE.compose(type, player, text);
            case OFFTOP:
                return OfftopComposer.INSTANCE.compose(type, player, text);
            case GM:
                return GmsayComposer.INSTANCE.compose(type, player, text);
        }
    }

    public static IChatComponent formatMessage(ChatMessage message) {
        switch (message.type) {
            default:
            case SPEECH:
                return SpeechComposer.INSTANCE.format(message);
            case OFFTOP:
                return OfftopComposer.INSTANCE.format(message);
            case GM:
                return GmsayComposer.INSTANCE.format(message);
        }
    }

    private static String getTextFromTranslation(ChatComponentTranslation chatComponent) {
        final Object formatObj = chatComponent.getFormatArgs()[1];
        if (formatObj instanceof IChatComponent)
            return ((IChatComponent) formatObj).getUnformattedText();
        else if (formatObj instanceof String)
            return (String) formatObj;
        else
            return chatComponent.getUnformattedText();
    }

    abstract ChatMessage compose(SpeechType type, EntityPlayer player, String text);

    abstract IChatComponent format(ChatMessage message);
}
