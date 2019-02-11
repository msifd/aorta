package msifeed.mc.aorta.chat.composer;

import msifeed.mc.aorta.chat.net.ChatMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;

interface ChatComposer {
    ChatMessage compose(EntityPlayer player, String text);

    IChatComponent format(EntityPlayer self, ChatMessage message);

    default boolean canReceiveMessage(EntityPlayer self, ChatMessage message) {
        return true;
    }
}
