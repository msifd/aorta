package msifeed.mc.aorta.core.utils;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import msifeed.mc.aorta.core.character.BodyPart;
import msifeed.mc.aorta.core.character.BodyShield;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.rolls.Roll;
import msifeed.mc.aorta.sys.utils.L10n;

import java.util.ArrayList;
import java.util.Map;

public class Differ {
    public static String diff(Character before, Character after) {
        final ArrayList<String> diffs = new ArrayList<>();

        if (!before.name.equalsIgnoreCase(after.name))
            diffs.add(L10n.fmt("aorta.diff.status.name", after.name));

        for (Map.Entry<Feature, Integer> e : after.features.entrySet()) {
            final int b = before.features.get(e.getKey());
            final int a = e.getValue();
            if (b != a)
                diffs.add(L10n.fmt("aorta.diff.char.feature", e.getKey().tr(), b, a));
        }

        final MapDifference<String, BodyPart> bpDiff = Maps.difference(before.bodyParts, after.bodyParts);
        for (String name : bpDiff.entriesOnlyOnRight().keySet()) {
            diffs.add(L10n.fmt("aorta.diff.char.add_bodypart", name));
        }
        for (String name : bpDiff.entriesOnlyOnLeft().keySet()) {
            diffs.add(L10n.fmt("aorta.diff.char.remove_bodypart", name));
        }
        for (String name : bpDiff.entriesDiffering().keySet()) {
            diffs.add(L10n.fmt("aorta.diff.char.update_bodypart", name));
        }

        if (before.vitalityRate != after.vitalityRate) {
            diffs.add(L10n.fmt("aorta.diff.char.vitality", before.vitalityRate, after.vitalityRate));
        }

        if (before.maxPsionics != after.maxPsionics) {
            diffs.add(L10n.fmt("aorta.diff.char.psionics", before.maxPsionics, after.maxPsionics));
        }

        if (before.sanity != after.sanity) {
            final int n = Math.abs(before.sanity - after.sanity);
            diffs.add(L10n.fmt(
                    before.sanity < after.sanity ? "aorta.diff.status.add_sanity" : "aorta.diff.status.rem_sanity",
                    n, trPoints(n)
            ));
        }

        if (before.psionics != after.psionics) {
            diffs.add(L10n.fmt(
                    before.psionics < after.psionics ? "aorta.diff.status.add_psionics" : "aorta.diff.status.rem_psionics",
                    Math.abs(before.psionics - after.psionics)
            ));
        }

        if (before.shield.type != after.shield.type) {
            if (before.shield.type == BodyShield.Type.NONE) {
                if (after.shield.type == BodyShield.Type.PSIONIC)
                    diffs.add(L10n.fmt("aorta.diff.status.cast_psionic_shield", after.shield.power));
                else
                    diffs.add(L10n.fmt("aorta.diff.status.enable_shield", after.shield.type.tr(), after.shield.power));
            } else if (after.shield.type == BodyShield.Type.NONE) {
                if (before.shield.type == BodyShield.Type.PSIONIC)
                    diffs.add(L10n.tr("aorta.diff.status.dispell_psionic_shield"));
                else
                    diffs.add(L10n.fmt("aorta.diff.status.disable_shield", before.shield.type.tr()));
            } else {
                diffs.add(L10n.fmt("aorta.diff.status.change_shield",
                        before.shield.type.tr(), before.shield.power,
                        after.shield.type.tr(), after.shield.power
                ));
            }
        } else if (before.shield.power != after.shield.power) {
            final int n = Math.abs(before.shield.power - after.shield.power);
            diffs.add(L10n.fmt(
                    before.shield.power < after.shield.power ? "aorta.diff.status.add_shield" : "aorta.diff.status.rem_shield",
                    n, trPoints(n)
            ));
        }

        for (Map.Entry<String, BodyPart> e : after.bodyParts.entrySet()) {
            final BodyPart b = before.bodyParts.get(e.getKey());
            final BodyPart a = e.getValue();
            if (b == null)
                continue;
            if (b.health != a.health) {
                final int n = Math.abs(b.health - a.health);
                final String line = b.health < a.health
                        ? "aorta.diff.status.add_health"
                        : b.health - a.health > a.armor
                        ? "aorta.diff.status.rem_health_wound"
                        : "aorta.diff.status.rem_health";
                diffs.add(L10n.fmt(line, e.getKey(), n, trPoints(n)));
            }
            if (b.armor != a.armor) {
                final int n = Math.abs(b.armor - a.armor);
                diffs.add(L10n.fmt(
                        b.armor < a.armor ? "aorta.diff.status.add_armor" : "aorta.diff.status.rem_armor",
                        e.getKey(), n, trPoints(n)
                ));
            }
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

        if (before.sanityLevel() != after.sanityLevel())
            diffs.add(L10n.fmt("aorta.diff.status.sanity_level", L10n.tr("aorta.diff.status.sanity." + after.sanityLevel())));

        final int vitalityBefore = before.vitalityLevel();
        final int vitalityAfter = after.vitalityLevel();
        if (vitalityAfter != vitalityBefore)
            diffs.add(L10n.fmt("aorta.diff.status.vitality_level", L10n.tr("aorta.diff.status.vitality." + vitalityAfter)));

        for (BodyPart abp : after.bodyParts.values()) {
            final BodyPart bbp = before.bodyParts.get(abp.name);
            if (bbp != null && abp.isInjured() && !bbp.isInjured())
                diffs.add(L10n.fmt("aorta.diff.status.injure", abp.name));
        }

        final int sanityModBefore = Roll.sanityMod(before.sanity);
        final int sanityModAfter = Roll.sanityMod(after.sanity);
        if (sanityModBefore != sanityModAfter) {
            // TODO: fill text
            diffs.add(L10n.fmt("aorta.diff.status.sanity_mod", sanityModAfter));
        }

        final int psionicsBefore = before.psionicsLevel();
        final int psionicsAfter = after.psionicsLevel();
        if (psionicsAfter != psionicsBefore) {
            if (psionicsAfter >= 4)
                diffs.add(L10n.tr("aorta.diff.status.psionics_critical"));
            else
                diffs.add(L10n.fmt("aorta.diff.status.psionics_level", psionicsAfter));
        }

        // Illness
        if (before.illness.level() != after.illness.level())
            diffs.add(L10n.fmt("aorta.diff.status.illness." + after.illness.level()));
        if (!before.illness.cured() && after.illness.cured())
            diffs.add(L10n.tr("aorta.diff.status.illness.cured"));
        if (!before.illness.lost() && after.illness.lost())
            diffs.add(L10n.tr("aorta.diff.status.illness.lost"));
        if (before.illness.debuff() != after.illness.debuff())
            diffs.add(L10n.fmt("aorta.diff.status.illness.debuff", after.illness.debuff()));

        return String.join(", ", diffs);
    }
}
