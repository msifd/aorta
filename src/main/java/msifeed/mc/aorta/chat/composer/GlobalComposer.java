package msifeed.mc.aorta.chat.composer;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.core.utils.MetaAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

class GlobalComposer implements ChatComposer {
    @Override
    public ChatMessage compose(EntityPlayer player, String text) {
        final ChatMessage message = new ChatMessage();
        message.type = SpeechType.GLOBAL;
        message.language = Language.VANILLA;
        message.radius = Aorta.DEFINES.get().chat.offtopRadius;
        message.speaker = player.getDisplayName();
        message.text = text;
        return message;
    }

    @Override
    public IChatComponent format(EntityPlayer self, ChatMessage message) {
        final String prefix = String.format("[GLOBAL] %s: %s", message.speaker, message.text);
        final ChatComponentText compPrefix = new ChatComponentText(prefix);
        compPrefix.getChatStyle().setColor(EnumChatFormatting.GREEN);
        return compPrefix;
    }

    @Override
    public boolean canReceiveMessage(EntityPlayer self, ChatMessage message) {
        return MetaAttribute.require(self).receiveGlobal;
    }
}
