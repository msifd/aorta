package msifeed.mc.aorta.chat.composer;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.core.rules.FeatureRollResult;
import msifeed.mc.aorta.utils.L10n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class RollComposer implements ChatComposer {
    @Override
    public ChatMessage compose(EntityPlayer player, String text) {
        final ChatMessage message = new ChatMessage();
        message.type = SpeechType.ROLL;
        message.language = Language.VANILLA;
        message.radius = Aorta.DEFINES.get().chat.rollRadius;
        message.speaker = player.getDisplayName();
        message.text = text;
        return message;
    }

    @Override
    public IChatComponent format(EntityPlayer self, ChatMessage message) {
        final ChatComponentText comp = new ChatComponentText(message.text);
        comp.getChatStyle().setColor(EnumChatFormatting.GOLD);
        return comp;
    }

    public static String makeText(EntityPlayer player, FeatureRollResult result) {
        final String featStr = L10n.tr("aorta.feature." + result.feature.name().toLowerCase());
        final String modStr = formatMod(result.mod);
        final String sanityStr = formatMod(result.sanityMod);
        return String.format("[ROLL] \u00a7r%s\u00a76: %s%s%s = %d", player.getDisplayName(), featStr, modStr, sanityStr, result.result);
    }

    private static String formatMod(int mod) {
        if (mod > 0)
            return " + " + mod;
        else if (mod < 0)
            return " - " + Math.abs(mod);
        else
            return "";
    }
}
