package msifeed.mc.aorta.chat;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;

public class ChatComponentComposer {
    public static void addNamePrefix(IChatComponent root, String name, boolean myName) {
        final ChatComponentText cName = new ChatComponentText(name);
        final ChatComponentText cSplitter = new ChatComponentText(": ");

        cName.getChatStyle().setColor(myName ? EnumChatFormatting.YELLOW : EnumChatFormatting.GREEN);

        addPrefix(root, cName, cSplitter);
    }

    public static void addPrefix(IChatComponent chatComponent, String prefix) {
        addPrefix(chatComponent, new ChatComponentText(prefix));
    }

    public static void addPrefix(IChatComponent chatComponent, IChatComponent... prefixes) {
        chatComponent.getSiblings().addAll(0, Arrays.asList(prefixes));
    }
}
