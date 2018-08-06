package msifeed.mc.aorta.chat;

import msifeed.mc.aorta.chat.net.SpeechMessage;
import msifeed.mc.aorta.core.attributes.TraitsAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SpeechFormatter {
    public static IChatComponent formatSpeech(SpeechMessage message) {
        switch (message.type) {
            case SPEECH:
                return formatSpeechMessage(message);
            case OFFTOP:
                return formatOfftopMessage(message);
            default:
                return null;
        }
    }

    private static IChatComponent formatSpeechMessage(SpeechMessage message) {
        String text = message.text;

//        changeGroups(chatComponent, '*', '*', c -> {
//            c.getChatStyle().setItalic(true);
//        });

        if (!isMyNameIs(message.speaker) && !doIKnowLanguage(message.language))
            text = message.language.obfuscator.obfuscate(text);

        final ChatComponentText root = new ChatComponentText("");
        root.appendText(text);

        if (!message.speaker.isEmpty())
            ChatComponentComposer.addNamePrefix(root, message.speaker, isMyNameIs(message.speaker));

        return root;
    }

    private static IChatComponent formatOfftopMessage(SpeechMessage message) {
        final String prefix = String.format("[OFF] %s: %s", message.speaker, message.text);
        final ChatComponentText compPrefix = new ChatComponentText(prefix);
        compPrefix.getChatStyle().setColor(EnumChatFormatting.GRAY);
        return compPrefix;
    }

    private static boolean isMyNameIs(String name) {
        return Minecraft.getMinecraft().thePlayer.getCommandSenderName().equalsIgnoreCase(name);
    }

    private static boolean doIKnowLanguage(Language language) {
        return language == Language.VANILLA || TraitsAttribute.INSTANCE.has(Minecraft.getMinecraft().thePlayer, language.trait);
    }
}
