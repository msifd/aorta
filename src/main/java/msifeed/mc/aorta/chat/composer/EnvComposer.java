package msifeed.mc.aorta.chat.composer;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.net.ChatMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class EnvComposer implements ChatComposer {
    @Override
    public ChatMessage compose(EntityPlayer player, String text) {
        final ChatMessage message = new ChatMessage();
        message.type = SpeechType.ENV;
        message.language = Language.VANILLA;
        message.radius = Aorta.DEFINES.get().chat.logRadius;
        message.speaker = "";
        message.text = text;
        return message;
    }

    @Override
    public IChatComponent format(EntityPlayer self, ChatMessage message) {
        final String prefix = String.format("[ENV] %s", message.text);
        final ChatComponentText compPrefix = new ChatComponentText(prefix);
        compPrefix.getChatStyle().setColor(EnumChatFormatting.WHITE);
        return compPrefix;
    }
}
