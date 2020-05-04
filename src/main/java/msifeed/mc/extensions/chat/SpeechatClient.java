package msifeed.mc.extensions.chat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.Bootstrap;
import msifeed.mc.commons.traits.Trait;
import msifeed.mc.extensions.chat.formatter.SpeechFormatter;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.GetUtils;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;

@SideOnly(Side.CLIENT)
final class SpeechatClient {
    static void receiveSpeech(int senderId, IChatComponent cc, int range) {
        final EntityPlayer self = Minecraft.getMinecraft().thePlayer;
        final EntityPlayer sender = GetUtils.entityPlayer(self, senderId).orElse(null);
        if (sender == null)
            return;
        displayMessage(self, SpeechFormatter.format(sender, cc, range));
    }

    static void receiveGlobal(IChatComponent cc) {
        final EntityPlayer self = Minecraft.getMinecraft().thePlayer;
        if (MetaAttribute.require(self).receiveGlobal)
            displayMessage(self, cc);
    }

    static void receiveGmGlobal(IChatComponent cc) {
        final EntityPlayer self = Minecraft.getMinecraft().thePlayer;
        if (CharacterAttribute.hasAny(self, Trait.__admin, Trait.gm))
            displayMessage(self, cc);
    }

    static void receiveRaw(IChatComponent cc) {
        final EntityPlayer self = Minecraft.getMinecraft().thePlayer;
        displayMessage(self, cc);
    }

    private static void displayMessage(EntityPlayer self, IChatComponent cc) {
        self.addChatMessage(cc);
        playNotificationSound(self, 1);
    }

//    private static void playNotificationSound(EntityPlayer self) {
//        GetUtils.entityLiving(self, message.senderId).ifPresent(speaker -> {
//            final float distance = self.getDistanceToEntity(speaker);
//            final float volume = (1 - distance / message.radius) + 0.4f;
//            self.playSound(Bootstrap.MODID + ":speechat.message", volume, 0.7F);
//        });
//    }

    private static void playNotificationSound(EntityPlayer self, float volume) {
        self.playSound(Bootstrap.MODID + ":speechat.message", volume, 0.7F);
    }
}
