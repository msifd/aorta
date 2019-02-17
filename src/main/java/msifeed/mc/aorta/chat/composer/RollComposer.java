package msifeed.mc.aorta.chat.composer;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.rules.Critical;
import msifeed.mc.aorta.core.rules.FeatureRoll;
import msifeed.mc.aorta.core.rules.FightRoll;
import msifeed.mc.aorta.core.rules.Roll;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static String makeText(EntityLivingBase entity, FeatureRoll roll) {
        final List<String> featNames = Stream.of(roll.features).map(Feature::shortName).collect(Collectors.toList());
        final String feat = String.join("+", featNames);
        return makeRollText("ROLL", getName(entity), feat, roll);
    }

    public static String makeText(EntityLivingBase entity, FightRoll roll) {
        return makeRollText("ACTION", getName(entity), roll.action.tr(), roll);
    }

    private static String makeRollText(String type, String player, String action, Roll roll) {
        final String modsStr = formatMod(roll.mod) + formatSanity(roll.sanity);
        if (modsStr.isEmpty()) {
            // [TYPE] player ACTION: res CRIT
            return String.format("[%s] \u00a7r%s\u00a76 %s: %d%s", type, player, action, roll.result, formatCrit(roll.critical));
        } else {
            // [TYPE] player ACTION: [roll] - mod - san = res CRIT
            return String.format("[%s] \u00a7r%s\u00a76 %s: [%s]%s = %d%s", type, player, action, roll.roll, modsStr, roll.result, formatCrit(roll.critical));
        }
    }

    private static String getName(EntityLivingBase entity) {
        return entity instanceof EntityPlayer ? ((EntityPlayer) entity).getDisplayName() : entity.getCommandSenderName();
    }

    private static String formatSanity(int san) {
        final String f = formatMod(san);
        return f.isEmpty() ? "" : "\u00a77" + f + "\u00a76";
    }

    private static String formatMod(int mod) {
        if (mod > 0)
            return " + " + mod;
        else if (mod < 0)
            return " - " + Math.abs(mod);
        else
            return "";
    }

    private static String formatCrit(Critical crit) {
        switch (crit) {
            default:
                return "";
            case LUCK:
                return " \u00a72LUCK";
            case FAIL:
                return " \u00a74FAIL";
        }
    }
}
