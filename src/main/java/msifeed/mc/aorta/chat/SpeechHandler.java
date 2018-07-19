package msifeed.mc.aorta.chat;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.aorta.chat.obfuscation.LangObfuscator;
import msifeed.mc.aorta.core.props.TraitsProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpeechHandler implements IMessageHandler<SpeechMessage, IMessage> {
    @Override
    public IMessage onMessage(SpeechMessage message, MessageContext ctx) {
        addChatMessage(message.speaker, message.language, message.chatComponent);
        return null;
    }

    public static void addChatMessage(String speaker, Language language, IChatComponent chatComponent) {
        if (!isMyNameIs(speaker) && !doIKnowLanguage(language))
            obfuscate(language.obfuscator, chatComponent);

        if (!speaker.isEmpty())
            addNamePrefix(speaker, chatComponent);

        Minecraft.getMinecraft().thePlayer.addChatMessage(chatComponent);
    }

    private static boolean doIKnowLanguage(Language language) {
        final TraitsProperty prop = TraitsProperty.get(Minecraft.getMinecraft().thePlayer);
        return prop != null && prop.has(language.trait);
    }

    private static void obfuscate(LangObfuscator obfuscator, IChatComponent chatComponent) {
        final List<ChatComponentText> texts = extractTextComponents(chatComponent);
        obfuscator.obfuscate(texts);
    }

    private static void addNamePrefix(String name, IChatComponent chatComponent) {
        final ChatComponentText cName = new ChatComponentText(name);
        final ChatComponentText cSplitter = new ChatComponentText(": ");

        cName.getChatStyle().setColor(isMyNameIs(name) ? EnumChatFormatting.YELLOW : EnumChatFormatting.GREEN);

        chatComponent.getSiblings().addAll(0, Arrays.asList(cName, cSplitter));
    }

    private static boolean isMyNameIs(String name) {
        return Minecraft.getMinecraft().thePlayer.getCommandSenderName().equalsIgnoreCase(name);
    }

    private static List<ChatComponentText> extractTextComponents(IChatComponent comp) {
        ArrayList<ChatComponentText> texts = new ArrayList<>();
        for (Object o : comp.getSiblings())
            if (o instanceof ChatComponentText)
                texts.add((ChatComponentText) o);
        return texts;
    }
}
