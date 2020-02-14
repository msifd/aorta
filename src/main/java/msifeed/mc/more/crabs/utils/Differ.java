package msifeed.mc.more.crabs.utils;

import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.extensions.chat.ChatHandler;
import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.composer.Composer;
import msifeed.mc.extensions.chat.composer.SpeechType;
import msifeed.mc.more.crabs.character.Ability;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.Map;

public class Differ {
    public static String diff(Character before, Character after) {
        final ArrayList<String> diffs = new ArrayList<>();

        if (!before.name.equalsIgnoreCase(after.name))
            diffs.add(L10n.fmt("aorta.diff.status.name", after.name));

        for (Map.Entry<Ability, Integer> e : after.abilities.entrySet()) {
            final int b = before.abilities.get(e.getKey());
            final int a = e.getValue();
            if (b != a)
                diffs.add(L10n.fmt("aorta.diff.char.feature", e.getKey().toString(), b, a));
        }

        // Illness
        if (before.illness.illness < after.illness.illness)
            diffs.add(L10n.fmt("aorta.diff.status.illness.add_illness", after.illness.illness - before.illness.illness));
        if (before.illness.illness > after.illness.illness)
            diffs.add(L10n.fmt("aorta.diff.status.illness.rem_illness", before.illness.illness - after.illness.illness));
        if (before.illness.treatment < after.illness.treatment)
            diffs.add(L10n.fmt("aorta.diff.status.illness.add_treatment", after.illness.treatment - before.illness.treatment));
        if (before.illness.treatment > after.illness.treatment)
            diffs.add(L10n.fmt("aorta.diff.status.illness.rem_treatment", before.illness.treatment - after.illness.treatment));

        if (before.estitence != after.estitence) {
            final int n = Math.abs(before.estitence - after.estitence);
            diffs.add(L10n.fmt(
                    before.estitence < after.estitence ? "aorta.diff.status.add_estitence" : "aorta.diff.status.rem_estitence",
                    n, trPoints(n)
            ));
        }

        if (before.sin != after.sin) {
            final int n = Math.abs(before.sin - after.sin);
            diffs.add(L10n.fmt(
                    before.sin < after.sin ? "aorta.diff.status.add_sin" : "aorta.diff.status.rem_sin",
                    n, trPoints(n)
            ));
        }

        return String.join(", ", diffs);
    }

    private static String trPoints(int points) {
        switch (points) {
            case 11:
            case 12:
            case 13:
            case 14:
                return L10n.tr("aorta.diff.m");
            default:
                switch (points % 10) {
                    case 0:
                        return L10n.tr("aorta.diff.m");
                    case 1:
                        return L10n.tr("aorta.diff.s");
                    case 2:
                    case 3:
                    case 4:
                        return L10n.tr("aorta.diff.g");
                    default:
                        return L10n.tr("aorta.diff.m");
                }
        }
    }

    public static String diffResults(Character before, Character after) {
        final ArrayList<String> diffs = new ArrayList<>();

        // Illness
        if (before.illness.level() != after.illness.level())
            diffs.add(L10n.fmt("aorta.diff.status.illness." + after.illness.level()));
        if (!before.illness.cured() && after.illness.cured())
            diffs.add(L10n.tr("aorta.diff.status.illness.cured"));
        if (!before.illness.lost() && after.illness.lost())
            diffs.add(L10n.tr("aorta.diff.status.illness.lost"));
        if (before.illness.debuff() != after.illness.debuff())
            diffs.add(L10n.fmt("aorta.diff.status.illness.debuff", after.illness.debuff()));

        final int hpBefore = before.countMaxHealth();
        final int hpAfter = after.countMaxHealth();
        if (hpBefore != hpAfter) {
            final int n = Math.abs(hpBefore - hpAfter);
            diffs.add(L10n.fmt(
                    hpBefore < hpAfter ? "aorta.diff.status.add_max_health" : "aorta.diff.status.rem_max_health", n
            ));
        }

        final int sinAfter = after.sinLevel();
        if (before.sinLevel() != sinAfter) {
            diffs.add(L10n.fmt("aorta.diff.status.sin_level", L10n.tr("aorta.status.sin." + sinAfter)));
        }

        return String.join(", ", diffs);
    }

    public static void printDiffs(EntityPlayerMP sender, Entity entity, Character before, Character after) {
        final String speaker = before.name.isEmpty() ? entity.getCommandSenderName() : before.name;
        final String logPrefix = sender == entity ? "" : "(" + sender.getDisplayName() + ") ";
        sendLogs(sender, speaker, logPrefix, diff(before, after));
        sendLogs(sender, speaker, logPrefix, diffResults(before, after));
    }

    private static void sendLogs(EntityPlayerMP sender, String speaker, String prefix, String message) {
        if (message.isEmpty())
            return;

        final ChatMessage m = Composer.makeMessage(SpeechType.LOG, sender, prefix + message);
        m.speaker = speaker;
        ChatHandler.sendSystemChatMessage(sender, m);

        ExternalLogs.log(sender, "log", message);
    }
}
