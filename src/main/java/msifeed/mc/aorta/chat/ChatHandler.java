package msifeed.mc.aorta.chat;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import msifeed.mc.aorta.network.Networking;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.ServerChatEvent;

import java.util.ArrayList;
import java.util.List;

public class ChatHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onChatMessageSent(ServerChatEvent event) {
//        final List<ChatComponentText> originalTexts = extractTextComponents(event.component);
//        LangObfuscator processor = new MenalaObfuscator();
//        final List<ChatComponentText> processedTexts = processor.obfuscate(originalTexts);
//        IChatComponent component = formatTexts(event.username, processedTexts);

        final SpeechMessage message = ChatMessageParser.parse(event.player, event.component);
        sendSpeechMessage(event.player, message);

        event.component = null;
    }

    public static void sendSpeechMessage(EntityPlayerMP sender, SpeechMessage message) {
        final NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(sender.dimension, sender.posX, sender.posY, sender.posZ, message.radius);
        Networking.CHANNEL.sendToAllAround(message, point);
    }

    private List<ChatComponentText> extractTextComponents(ChatComponentTranslation comp) {
        ArrayList<ChatComponentText> texts = new ArrayList<>();
        for (Object o : ((IChatComponent) comp.getFormatArgs()[1]).getSiblings())
            if (o instanceof ChatComponentText)
                texts.add((ChatComponentText) o);
        return texts;
    }

    private ChatComponentTranslation formatTexts(String username, List<ChatComponentText> processedTexts) {
        final ChatComponentText textCompound = new ChatComponentText("");
        for (IChatComponent c : processedTexts)
            textCompound.appendSibling(c);
        return new ChatComponentTranslation("aorta.chat.text", formatUsername(username), textCompound);
    }

    private ChatComponentText formatUsername(String username) {
        final ChatComponentText c = new ChatComponentText(username);
        c.getChatStyle().setColor(EnumChatFormatting.GREEN);
        return c;
    }
}
