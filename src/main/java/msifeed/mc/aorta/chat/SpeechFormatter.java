package msifeed.mc.aorta.chat;

import msifeed.mc.aorta.chat.net.SpeechMessage;
import msifeed.mc.aorta.chat.obfuscation.LangObfuscator;
import msifeed.mc.aorta.core.props.TraitsAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class SpeechFormatter {
    public static void formatSpeech(SpeechMessage message) {
        switch (message.type) {
            case SPEECH:
                formatSpeechMessage(message);
                break;
            case OFFTOP:
                formatOfftopMessage(message);
                break;
        }
    }

    public static void formatSpeechMessage(SpeechMessage message) {
        final IChatComponent chatComponent = message.chatComponent;

        // Ох, чтобы я без вас делала, сударь! *отсасывает собеседнику* Нямушки.

        changeGroups(chatComponent, '*', '*', c -> {
            c.getChatStyle().setItalic(true);
        });

//        if (!isMyNameIs(speaker) && !doIKnowLanguage(language))
        if (!doIKnowLanguage(message.language))
            obfuscate(message.language.obfuscator, chatComponent);

        if (!message.speaker.isEmpty())
            SpeechFormatter.addNamePrefix(chatComponent, message.speaker, isMyNameIs(message.speaker));
    }

    public static void formatOfftopMessage(SpeechMessage message) {
        final String prefix = String.format("[OFF] %s: ", message.speaker);
        final ChatComponentText compPrefix = new ChatComponentText(prefix);
        compPrefix.getChatStyle().setColor(EnumChatFormatting.GRAY);
        addPrefix(message.chatComponent, compPrefix);
    }

    private static boolean isMyNameIs(String name) {
        return Minecraft.getMinecraft().thePlayer.getCommandSenderName().equalsIgnoreCase(name);
    }

    private static boolean doIKnowLanguage(Language language) {
        return language == Language.VANILLA || TraitsAttribute.INSTANCE.has(Minecraft.getMinecraft().thePlayer, language.trait);
    }

    private static void obfuscate(LangObfuscator obfuscator, IChatComponent chatComponent) {
        final List<ChatComponentText> texts = SpeechFormatter.extractTextComponents(chatComponent);
        obfuscator.obfuscate(texts);
    }

    public static void addNamePrefix(IChatComponent chatComponent, String name, boolean myName) {
        final ChatComponentText cName = new ChatComponentText(name);
        final ChatComponentText cSplitter = new ChatComponentText(": ");

        cName.getChatStyle().setColor(myName ? EnumChatFormatting.YELLOW : EnumChatFormatting.GREEN);

        addPrefix(chatComponent, cName, cSplitter);
    }

    public static void addPrefix(IChatComponent chatComponent, IChatComponent... prefixes) {
        chatComponent.getSiblings().addAll(0, Arrays.asList(prefixes));
    }

    private static void changeGroups(IChatComponent chatComponent, char start, char end, Consumer<ChatComponentText> fn) {
        while (true) {
            ChatComponentText extracted = null;
            final List<ChatComponentText> texts = extractTextComponents(chatComponent);
            for (ChatComponentText c : texts) {
                extracted = extractGroup(c, start, end);
                if (extracted != null) {
                    fn.accept(extracted);
                    break;
                }
            }
            if (extracted == null)
                break;
        }
    }

    private static ChatComponentText extractGroup(ChatComponentText chatComponent, char start, char end) {
        final String text = chatComponent.getUnformattedTextForChat();
        int groupStart = -1;

        ChatComponentText extracted = null;

        for (int i = 0; i < text.length(); ++i) {
            final char c = text.charAt(i);
            if (c == start && groupStart == -1)
                groupStart = i;
            else if (c == end && groupStart >= 0) {
                final String pre = text.substring(0, groupStart);
                final String group = text.substring(groupStart, i + 1);
                final String post = text.substring(i + 1);

                extracted = new ChatComponentText(group);

                ChatUtils.setText(chatComponent, "");
                chatComponent.appendText(pre);
                chatComponent.appendSibling(extracted);
                chatComponent.appendText(post);

                break;
            }
        }

        return extracted;
    }

    public static List<ChatComponentText> extractTextComponents(IChatComponent chatComponent) {
        ArrayList<ChatComponentText> texts = new ArrayList<>();
        ArrayList<IChatComponent> current = new ArrayList<>();
        ArrayList<IChatComponent> next = new ArrayList<>();

        current.addAll(chatComponent.getSiblings());

        while (!current.isEmpty()) {
            for (IChatComponent c : current) {
                if (c.getSiblings().isEmpty()) {
                    if (c instanceof ChatComponentText && c.getChatStyle().isEmpty())
                        texts.add((ChatComponentText) c);
                } else {
                    next.addAll(c.getSiblings());
                }
            }

            current.clear();
            current.addAll(next);
            next.clear();
        }

        return texts;
    }
}
