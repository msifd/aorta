package msifeed.mc.aorta.chat.composer;

import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.gm.GmSpeech;
import msifeed.mc.aorta.chat.gm.GmsaySettings;
import msifeed.mc.aorta.chat.net.ChatMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.regex.Pattern;

class GmsayComposer implements ChatComposer {
    private static final Pattern colorsOnlyPattern = Pattern.compile("(\\u00a7[0-9A-FK-ORa-fk-or])*");

    @Override
    public ChatMessage compose(EntityPlayer player, String text) {
        final GmsaySettings settings = GmSpeech.get(player.getCommandSenderName());

        final ChatMessage message = new ChatMessage();
        message.type = SpeechType.GM;
        message.language = Language.VANILLA;
        message.radius = settings.radius;
        message.speaker = player.getCommandSenderName();
        message.text = colorsOnlyPattern.matcher(settings.prefix).matches()
                ? settings.prefix + text
                : settings.prefix + ' ' + text;
        return message;
    }

    @Override
    public IChatComponent format(EntityPlayer self, ChatMessage message) {
        return new ChatComponentText(message.text);
    }
}
