package msifeed.mc.aorta.chat;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import msifeed.mc.aorta.chat.composer.Composer;
import msifeed.mc.aorta.chat.composer.SpeechType;
import msifeed.mc.aorta.chat.gm.GmSpeech;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.logs.Logs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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

    public static void sendSystemChatMessage(EntityPlayer at, String type, ChatMessage message) {
        final NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(at.dimension, at.posX, at.posY, at.posZ, message.radius);
        Speechat.CHANNEL.sendToAllAround(message, point);
        Logs.log(at, type, message.text);
    }

    public static void sendChatMessage(EntityPlayerMP sender, ChatMessage message) {
        final NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(sender.dimension, sender.posX, sender.posY, sender.posZ, message.radius);
        Speechat.CHANNEL.sendToAllAround(message, point);
        final String langPrefix = message.language == Language.VANILLA || message.language == Language.COMMON
                ? "" : String.format("[%s] ", message.language.shortTr());
        Logs.log(sender, "chat", langPrefix + message.text);
    }

    public static void sendGlobalChatMessage(EntityPlayerMP sender, ChatMessage message) {
        Speechat.CHANNEL.sendToAll(message);
        Logs.log(sender, "chat.global", message.text);
    }
}
