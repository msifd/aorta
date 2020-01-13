package msifeed.mc.extensions.chat.composer;

import msifeed.mc.extensions.chat.ChatMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;

interface ChatComposer {
    ChatMessage compose(EntityPlayer player, String text);

    IChatComponent format(EntityPlayer self, ChatMessage message);

    default boolean canReceiveMessage(EntityPlayer self, ChatMessage message) {
        return true;
    }
}
