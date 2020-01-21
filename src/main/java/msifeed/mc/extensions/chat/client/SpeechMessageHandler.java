package msifeed.mc.extensions.chat.client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.composer.Composer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;

public class SpeechMessageHandler implements IMessageHandler<ChatMessage, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(ChatMessage message, MessageContext ctx) {
        final EntityPlayer player = net.minecraft.client.Minecraft.getMinecraft().thePlayer;
        if (Composer.canReceiveMessage(player, message)) {
            final IChatComponent chatComponent = Composer.formatMessage(player, message);
            player.addChatMessage(chatComponent);
        }
        return null;
    }
}
