package msifeed.mc.more.crabs.combat;

import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.extensions.chat.ChatHandler;
import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.Language;
import msifeed.mc.extensions.chat.composer.SpeechType;
import msifeed.mc.more.More;
import msifeed.mc.more.crabs.action.ActionHeader;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.rolls.Criticalness;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.more.crabs.utils.GetUtils;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

public final class CombatNotifications {
    public static void actionChanged(EntityPlayer sender, EntityLivingBase target, ActionHeader action) {
        notify(sender, "§fВыбрано действие " + action.getTitle());
    }

    static void notifyKnockedOut(FighterInfo self) {
        notifyAroundRelatives(self, L10n.fmt("more.crabs.knocked_out", getName(self.entity)));
    }

    static void notifyKilled(FighterInfo self) {
        notifyAroundRelatives(self, L10n.fmt("more.crabs.killed", getName(self.entity)));
    }

    static void actionResult(FighterInfo winner, FighterInfo looser) {
        // Chad - LUCK Punch + Stun [50] (+5 STR+99) > Virgin - Roll [10]
        final String text = formatAction(winner) + " > " + formatAction(looser);
        notifyAroundRelatives(winner, text);
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
            sb.append("ПРОВАЛ ");
        else if (act.critical == Criticalness.LUCK)
            sb.append("УДАЧА ");

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

    public static String getName(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer)
            return ((EntityPlayer) entity).getDisplayName();

        final Character chr = CharacterAttribute.get(entity).orElse(null);
        if (chr != null && !chr.name.isEmpty())
            return chr.name;

        return entity.getCommandSenderName();
    }

    static void notify(EntityLivingBase entity, String text) {
        final ChatMessage message = new ChatMessage();
        message.type = SpeechType.COMBAT;
        message.language = Language.VANILLA;
        message.radius = More.DEFINES.get().chat.combatRadius;
        message.senderId = entity.getEntityId();
        message.speaker = "";
        message.text = text;

        ChatHandler.sendSystemChatMessage(entity, message);
        ExternalLogs.logEntity(entity, "combat", text);
    }

    private static void notifyAroundRelatives(FighterInfo cause, String text) {
        final ChatMessage message = new ChatMessage();
        message.type = SpeechType.COMBAT;
        message.language = Language.VANILLA;
        message.radius = More.DEFINES.get().chat.combatRadius;
        message.senderId = cause.entity.getEntityId();
        message.speaker = "";
        message.text = text;

        final HashSet<EntityLivingBase> relatives = new HashSet<>();
        final CombatContext offenderCom;

        if (cause.com.role == CombatContext.Role.DEFENCE) {
            if (cause.com.targets.isEmpty())
                return;
            final EntityLivingBase off = GetUtils.entityLiving(cause.entity, cause.com.targets.get(0)).orElse(null);
            if (off == null)
                return;
            relatives.add(off);
            offenderCom = CombatAttribute.get(off).orElse(cause.com);
        } else {
            relatives.add(cause.entity);
            offenderCom = cause.com;
        }

        offenderCom.targets.stream()
                .map(id -> GetUtils.entityLiving(cause.entity, id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(relatives::add);

        if (relatives.isEmpty())
            return;

        long avgX = 0, avgY = 0, avgZ = 0;
        for (EntityLivingBase e : relatives) {
            avgX += e.posX;
            avgY += e.posY;
            avgZ += e.posZ;
        }
        avgX /= relatives.size();
        avgY /= relatives.size();
        avgZ /= relatives.size();
        final ChunkCoordinates center = new ChunkCoordinates((int) avgX, (int) avgY, (int) avgZ);

        for (EntityLivingBase e : relatives) {
            final double dist = e.getDistance(avgX, avgY, avgZ);
            message.radius = Math.max(message.radius, MathHelper.ceiling_double_int(dist) + 5);
        }

        ChatHandler.sendSystemChatMessage(cause.entity.dimension, center, message);
        ExternalLogs.logEntity(cause.entity, "combat", text);
    }
}
