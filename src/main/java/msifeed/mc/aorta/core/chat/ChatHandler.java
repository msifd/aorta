package msifeed.mc.aorta.core.chat;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.*;
import net.minecraftforge.event.ServerChatEvent;

import java.util.ArrayList;
import java.util.List;

public class ChatHandler {
    @SubscribeEvent
    public void onChatMessageSent(ServerChatEvent event) {
        final List<ChatComponentText> originalTexts = extractTextComponents(event.component);

        LangProcessor processor = new LangMenala();
        final List<ChatComponentText> processedTexts = processor.process(originalTexts);

        event.component = formatTexts(event.username, processedTexts);
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
