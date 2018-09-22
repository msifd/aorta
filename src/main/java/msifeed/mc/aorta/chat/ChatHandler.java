package msifeed.mc.aorta.chat;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import msifeed.mc.aorta.chat.composer.ChatMessageComposer;
import msifeed.mc.aorta.chat.composer.SpeechType;
import msifeed.mc.aorta.chat.gm.GmSpeech;
import msifeed.mc.aorta.chat.net.ChatMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.ServerChatEvent;

public class ChatHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onChatMessageSent(ServerChatEvent event) {
        final SpeechType type = GmSpeech.shouldUseGmsay(event.player) ? SpeechType.GM : SpeechType.SPEECH;
        final ChatMessage message = ChatMessageComposer.makeMessage(type, event.player, event.component);
        sendSpeechMessage(event.player, message);
        event.component = null;
        event.setCanceled(true);
    }

    public static void sendSpeechMessage(EntityPlayerMP sender, ChatMessage message) {
        final NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(sender.dimension, sender.posX, sender.posY, sender.posZ, message.radius);
        Speechat.CHANNEL.sendToAllAround(message, point);
    }
}
