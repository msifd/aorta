package msifeed.mc.extensions.chat.client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.Bootstrap;
import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.composer.Composer;
import msifeed.mc.extensions.chat.composer.SpeechType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;

public class SpeechMessageHandler implements IMessageHandler<ChatMessage, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(ChatMessage message, MessageContext ctx) {
        final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (Composer.canReceiveMessage(player, message)) {
            final IChatComponent chatComponent = Composer.formatMessage(player, message);
            player.addChatMessage(chatComponent);

            if (message.type == SpeechType.SPEECH || message.type == SpeechType.GM) {
                final Entity speaker = player.worldObj.getEntityByID(message.senderId);
                if (speaker != null) {
                    final float distance = player.getDistanceToEntity(speaker);
                    final float volume = (1 - distance / message.radius) + 0.4f;
                    player.playSound(Bootstrap.MODID + ":speechat.message", volume, 0.7F);
                }
            }
        }
        return null;
    }
}
