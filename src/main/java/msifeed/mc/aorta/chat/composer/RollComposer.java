package msifeed.mc.aorta.chat.composer;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.core.rules.FeatureRoll;
import msifeed.mc.aorta.core.rules.FightRoll;
import msifeed.mc.aorta.utils.L10n;
import net.minecraft.entity.EntityLivingBase;
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

    public static String makeText(EntityLivingBase entity, FeatureRoll result) {
        final String action = L10n.tr("aorta.feature." + result.feature.name().toLowerCase());
        return makeText("ROLL", getName(entity), action, result.roll, result.mod, result.sanity, result.result);
    }

    public static String makeText(EntityLivingBase entity, FightRoll result) {
        final String action = L10n.tr("aorta.action." + result.action.name().toLowerCase());
        return makeText("ACTION", getName(entity), action, result.roll, result.mod, result.sanity, result.result);
    }

    private static String makeText(String type, String name, String action, int roll, int mod, int sanity, int result) {
        if (mod != 0 || sanity != 0) {
            final String modStr = formatMod(mod);
            final String sanityStr = formatMod(sanity);
            // [ACTION] username Hit: [5] - 1 - 1 = 3
            return String.format("[%s] \u00a7r%s\u00a76 %s: %d%s%s = %d", type, name, action, roll, modStr, sanityStr, result);
        } else {
            // [ACTION] username Hit: [5]
            return String.format("[%s] \u00a7r%s\u00a76 %s: %d", type, name, action, result);
        }
    }

    private static String getName(EntityLivingBase entity) {
        return entity instanceof EntityPlayer ? ((EntityPlayer) entity).getDisplayName() : entity.getCommandSenderName();
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
