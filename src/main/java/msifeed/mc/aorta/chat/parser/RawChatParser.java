package msifeed.mc.aorta.chat.parser;

import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.net.SpeechMessage;
import msifeed.mc.aorta.chat.usage.LangAttribute;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

public class RawChatParser {
    private static final int[] LOUDNESS_RADIUS = {2, 5, 15, 30, 60};

    public static SpeechMessage parse(EntityPlayerMP sender, ChatComponentTranslation chatComponent) {
        final String text = getTextFromTranslation(chatComponent);
        final SpeechMessage message = new SpeechMessage();
        message.language = LangAttribute.INSTANCE.get(sender).orElse(Language.MENALA);
        message.radius = getSpeechRadius(text);
        message.speaker = sender.getDisplayName();
        message.text = text;
        return message;
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

    private static int getSpeechRadius(String text) {
        int loudness = (LOUDNESS_RADIUS.length - 1) / 2;

        int exclamations = 0;
        for (int i = text.length() - 1; i >= 0; --i) {
            switch (text.charAt(i)) {
                case '!':
                    exclamations++;
                case '?':
                    continue;
            }
            break;
        }
        loudness += exclamations;

        if (exclamations == 0) {
            int silencers = 0;
            for (int i = 0; i < text.length(); ++i) {
                if (text.charAt(i) == '(' && text.charAt(text.length() - i - 1) == ')')
                    silencers++;
                else
                    break;
            }
            loudness -= silencers;
        }

        loudness = MathHelper.clamp_int(loudness, 0, LOUDNESS_RADIUS.length - 1);

        return LOUDNESS_RADIUS[loudness];
    }
}
