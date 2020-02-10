package msifeed.mc.extensions.chat.composer;

import msifeed.mc.commons.traits.Trait;
import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.Language;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

class GmGlobalComposer implements ChatComposer {
    @Override
    public ChatMessage compose(EntityPlayer player, String text) {
        final ChatMessage message = new ChatMessage();
        message.type = SpeechType.GM_GLOBAL;
        message.language = Language.VANILLA;
        message.radius = 0;
        message.speaker = player.getDisplayName();
        message.text = text;
        return message;
    }

    @Override
    public IChatComponent format(EntityPlayer self, ChatMessage message) {
        final String prefix = String.format("[GM] %s: %s", message.speaker, message.text);
        final ChatComponentText compPrefix = new ChatComponentText(prefix);
        compPrefix.getChatStyle().setColor(EnumChatFormatting.DARK_PURPLE);
        return compPrefix;
    }

    @Override
    public boolean canReceiveMessage(EntityPlayer self, ChatMessage message) {
        return CharacterAttribute.has(self, Trait.gm);
    }
}
