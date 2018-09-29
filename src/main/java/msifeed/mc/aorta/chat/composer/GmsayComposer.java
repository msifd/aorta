package msifeed.mc.aorta.chat.composer;

import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.gm.GmSpeech;
import msifeed.mc.aorta.chat.gm.GmsaySettings;
import msifeed.mc.aorta.chat.net.ChatMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

class GmsayComposer implements ChatComposer {
    @Override
    public ChatMessage compose(EntityPlayer player, String text) {
        final GmsaySettings settings = GmSpeech.get(player.getCommandSenderName());

        final boolean prefixWithColorOnly = settings.prefix.isEmpty() != EnumChatFormatting.getTextWithoutFormattingCodes(settings.prefix).isEmpty();

        final ChatMessage message = new ChatMessage();
        message.type = SpeechType.GM;
        message.language = Language.VANILLA;
        message.radius = settings.radius;
        message.speaker = player.getCommandSenderName();
        message.text = prefixWithColorOnly || settings.prefix.isEmpty()
                ? settings.prefix + text
                : settings.prefix + ' ' + text;
        return message;
    }

    @Override
    public IChatComponent format(EntityPlayer self, ChatMessage message) {
        return new ChatComponentText(message.text);
    }
}
