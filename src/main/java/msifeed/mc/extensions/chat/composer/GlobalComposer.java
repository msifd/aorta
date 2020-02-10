package msifeed.mc.extensions.chat.composer;

import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.Language;
import msifeed.mc.more.More;
import msifeed.mc.more.crabs.utils.MetaAttribute;
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
        message.radius = More.DEFINES.get().chat.offtopRadius;
        message.senderId = player.getEntityId();
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
