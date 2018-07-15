package msifeed.mc.aorta.core.chat;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class ChatHandlerClient {
    @SubscribeEvent
    public void onChatMessageReceived(ClientChatReceivedEvent event) {
        if (!(event.message instanceof ChatComponentTranslation))
            return;

        final ChatComponentTranslation msg = (ChatComponentTranslation) event.message;

        if (isMyMessage(msg))
            formatToSenderMessage(msg);
    }

    private boolean isMyMessage(ChatComponentTranslation comp) {
        final Object nameObj = comp.getFormatArgs()[0];
        if (nameObj instanceof ChatComponentText) {
            final String myName = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
            return ((ChatComponentText) nameObj).getUnformattedTextForChat().equals(myName);
        }
        return false;
    }

    private void formatToSenderMessage(ChatComponentTranslation comp) {
        final Object nameObj = comp.getFormatArgs()[0];
        if (nameObj instanceof IChatComponent) {
            ((IChatComponent) nameObj).getChatStyle().setColor(EnumChatFormatting.YELLOW);
        }
    }
}
