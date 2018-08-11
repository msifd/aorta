package msifeed.mc.aorta.chat;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import msifeed.mc.aorta.chat.net.SpeechMessage;
import msifeed.mc.aorta.chat.parser.RawChatParser;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.ServerChatEvent;

public class ChatHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onChatMessageSent(ServerChatEvent event) {
        final SpeechMessage message = RawChatParser.parse(event.player, event.component);
        sendSpeechMessage(event.player, message);
        event.component = null;
    }

    public static void sendSpeechMessage(EntityPlayerMP sender, SpeechMessage message) {
        final NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(sender.dimension, sender.posX, sender.posY, sender.posZ, message.radius);
        Speechat.CHANNEL.sendToAllAround(message, point);
    }
}
