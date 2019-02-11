package msifeed.mc.aorta.chat.composer;

import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.traits.Trait;
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
