package msifeed.mc.extensions.chat.composer;

import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.Language;
import msifeed.mc.more.More;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CombatComposer implements ChatComposer {
    @Override
    public ChatMessage compose(EntityPlayer player, String text) {
        final ChatMessage message = new ChatMessage();
        message.type = SpeechType.COMBAT;
        message.language = Language.VANILLA;
        message.radius = More.DEFINES.get().chat.combatRadius;
        message.speaker = "";
        message.text = text;
        return message;
    }

    @Override
    public IChatComponent format(EntityPlayer self, ChatMessage message) {
        final String prefix = String.format("[COMBAT] %s", message.text);
        final ChatComponentText comp = new ChatComponentText(prefix);
        comp.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        return comp;
    }
}
