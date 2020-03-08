package msifeed.mc.extensions.chat;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.extensions.chat.composer.Composer;
import msifeed.mc.extensions.chat.composer.SpeechType;
import msifeed.mc.extensions.chat.gm.GmSpeech;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ServerChatEvent;

public class ChatHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onChatMessageSent(ServerChatEvent event) {
        final SpeechType type = GmSpeech.shouldUseGmsay(event.player) ? SpeechType.GM : SpeechType.SPEECH;
        final ChatMessage message = Composer.makeMessage(type, event.player, event.component);
        sendChatMessage(event.player, message);
        event.component = null;
        event.setCanceled(true);
    }

    public static void sendSystemChatMessage(Entity at, ChatMessage message) {
        final NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(at.dimension, at.posX, at.posY, at.posZ, message.radius);
        Speechat.CHANNEL.sendToAllAround(message, point);
    }

    public static void sendChatMessage(EntityPlayer sender, ChatMessage message) {
        final NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(sender.dimension, sender.posX, sender.posY, sender.posZ, message.radius);
        Speechat.CHANNEL.sendToAllAround(message, point);
        final String langPrefix = "";
//        final String langPrefix = message.language == Language.VANILLA || message.language == Language.COMMON
//                ? "" : String.format("[%s] ", message.language.shortTr());
        ExternalLogs.log(sender, message.type.toString().toLowerCase(), langPrefix + message.text);
    }

    public static void sendGlobalChatMessage(EntityPlayer sender, ChatMessage message) {
        Speechat.CHANNEL.sendToAll(message);
        ExternalLogs.log(sender, message.type.toString().toLowerCase(), message.text);
    }
}
