package msifeed.mc.aorta.chat;

import msifeed.mc.aorta.chat.net.SpeechMessage;
import msifeed.mc.aorta.chat.obfuscation.LangObfuscator;
import msifeed.mc.aorta.core.attributes.TraitsAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

//        if (!isMyNameIs(message.speaker) && !doIKnowLanguage(message.language))
            text = obfuscateWith(message.language.obfuscator, text);

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
        return TraitsAttribute.INSTANCE.has(Minecraft.getMinecraft().thePlayer, language.trait);
    }

    private static String obfuscateWith(LangObfuscator obfuscator, String text) {
        final List<String> parts = splitToParts(text);
        return joinParts(obfuscator.obfuscate(parts));
    }

    private static List<String> splitToParts(String text) {
        final ArrayList<String> parts = new ArrayList<>();
        final StringBuilder sb = new StringBuilder();

        boolean prevLetter = false;
        for (int code : text.codePoints().toArray()) {
            final boolean currLetter = Character.isLetter(code);
            if (prevLetter != currLetter && sb.length() > 0) {
                parts.add(sb.toString());
                sb.setLength(0);
            }
            sb.appendCodePoint(code);
            prevLetter = currLetter;
        }

        if (sb.length() > 0)
            parts.add(sb.toString());

        return parts;
    }

    private static String joinParts(List<String> words) {
        return words.stream().collect(Collectors.joining());
    }
}
