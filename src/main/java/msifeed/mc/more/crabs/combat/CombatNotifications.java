package msifeed.mc.more.crabs.combat;

import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.extensions.chat.ChatHandler;
import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.Language;
import msifeed.mc.extensions.chat.composer.SpeechType;
import msifeed.mc.more.crabs.action.ActionHeader;
import msifeed.mc.more.crabs.rolls.Criticalness;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.stream.Collectors;

public final class CombatNotifications {
    public static void actionChanged(EntityPlayer sender, EntityLivingBase target, ActionHeader action) {
        notify(sender, "§f выбрано действие " + action.getTitle());
    }

    static void notifyKnockedOut(EntityLivingBase entity) {
        notify(entity, L10n.fmt("more.crabs.knocked_out", getName(entity)));
    }

    static void notifyKilled(EntityLivingBase entity) {
        notify(entity, "is killed!");
    }

    static void moveResult(FighterInfo winner, FighterInfo looser) {
        // Chad - LUCK Punch + Stun [50] (+5 STR+99) > Virgin - Roll [10]
        final String text = formatAction(winner) + " > " + formatAction(looser);
        notify(winner.entity, text);
    }

    static void soloMoveResult(FighterInfo info) {
        final String name = info.entity instanceof EntityPlayer
                ? ((EntityPlayer) info.entity).getDisplayName() : info.entity.getCommandSenderName();

        // Virgin - FAIL Punch
        final String text = formatAction(info);

        notify(info.entity, text);
    }

    private static String formatAction(FighterInfo info) {
        return getName(info.entity) + " - " + formatScores(info);
    }

    private static String formatScores(FighterInfo info) {
        final ActionContext act = info.act;
        final StringBuilder sb = new StringBuilder();

        if (!act.successful || act.critical == Criticalness.FAIL)
            sb.append("FAIL ");
        else if (act.critical == Criticalness.LUCK)
            sb.append("LUCK ");

        sb.append(act.action.getTitle());
        if (info.comboAction != null) {
            sb.append(" + ");
            sb.append(info.comboAction.getTitle());
        }

        if (act.score() > 0) {
            if (act.scoreAction > 0) {
                sb.append(" [");
                sb.append(act.scoreAction);
                sb.append(']');
            }

            if (!info.mod.isZeroed()) {
                sb.append(" (");
                if (info.mod.roll != 0)
                    sb.append(explicitSignInt(info.mod.roll));
                if (info.mod.hasAbilityMods()) {
                    if (info.mod.roll != 0)
                        sb.append(' ');
                    sb.append(info.mod.abilities.entrySet().stream()
                            .filter(e -> e.getValue() != 0)
                            .map(e -> e.getKey().trShort() + explicitSignInt(e.getValue()))
                            .collect(Collectors.joining(" ")));
                }
                sb.append(')');
            }
        }

        return sb.toString();
    }

    public static String explicitSignInt(int i) {
        return String.format("%+d", i);
    }

    private static String getName(EntityLivingBase entity) {
        return entity instanceof EntityPlayer
                ? ((EntityPlayer) entity).getDisplayName()
                : entity.getCommandSenderName();
    }

    static void notify(EntityLivingBase entity, String text) {
        final ChatMessage message = new ChatMessage();
        message.type = SpeechType.COMBAT;
        message.language = Language.VANILLA;
        message.radius = 15;
        message.senderId = entity.getEntityId();
        message.speaker = "";
        message.text = text;

        ChatHandler.sendSystemChatMessage(entity, message);
        ExternalLogs.logEntity(entity, "combat", text);
    }
}
