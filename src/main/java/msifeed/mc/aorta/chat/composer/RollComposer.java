package msifeed.mc.aorta.chat.composer;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.rolls.*;
import msifeed.mc.aorta.sys.utils.L10n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.Map;

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

    public static String makeText(EntityLivingBase entity, Character character, FeatureRoll roll) {
        return makeRollText("ROLL", getName(entity, character), roll.feature.trShort(), roll);
    }

    public static String makeText(EntityLivingBase entity, Character character, FightRoll roll) {
        return makeRollText("ACTION", getName(entity, character), roll.action.tr(), roll);
    }

    private static String makeRollText(String type, String player, String action, Roll roll) {
        final String actionStr = roll.target.isEmpty() ? action : action + " в " + roll.target;
        final String modsStr = formatNumber(roll.mods.rollMod) + formatStatusMod(roll.statusMod);
        if (modsStr.isEmpty()) {
            // [TYPE] player ACTION: (feat mods) res CRIT
            return String.format("[%s] \u00a7r%s\u00a76 %s:%s %d%s", type, player, actionStr, formatFeatMods(roll.mods), roll.result, formatCrit(roll.critical));
        } else {
            // [TYPE] player ACTION: (feat mods) [roll] - mod - stat = res CRIT
            return String.format("[%s] \u00a7r%s\u00a76 %s:%s [%s]%s = %d%s", type, player, actionStr, formatFeatMods(roll.mods), roll.roll, modsStr, roll.result, formatCrit(roll.critical));
        }
    }

    private static String getName(EntityLivingBase entity, Character character) {
        return character.name.isEmpty() ? entity.getCommandSenderName() : character.name;
    }

    private static String formatFeatMods(Modifiers mod) {
        final StringBuilder sb = new StringBuilder();

        for (Map.Entry<Feature, Integer> e : mod.featureMods.entrySet()) {
            if (e.getValue() == 0)
                continue;
            sb.append(e.getKey().trShort());
            sb.append(e.getValue() > 0 ? '+' : '-');
            sb.append(Math.abs(e.getValue()));
            sb.append(' ');
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // remove whitespace
            return " (" + sb.toString() + ')';
        } else {
            return "";
        }
    }

    private static String formatStatusMod(int san) {
        final String f = formatNumber(san);
        return f.isEmpty() ? "" : "\u00a77" + f + "\u00a76";
    }

    private static String formatNumber(int num) {
        if (num > 0)
            return " + " + num;
        else if (num < 0)
            return " - " + Math.abs(num);
        else
            return "";
    }

    private static String formatCrit(Critical crit) {
        switch (crit) {
            default:
                return "";
            case LUCK:
                return " \u00a72" + L10n.tr("aorta.gui.roller.luck");
            case FAIL:
                return " \u00a74" + L10n.tr("aorta.gui.roller.fail");
        }
    }
}
