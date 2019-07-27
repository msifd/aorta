package msifeed.mc.aorta.core.character;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import msifeed.mc.aorta.sys.utils.L10n;

import java.util.ArrayList;
import java.util.Map;

class Differ {
    static String diff(Character before, Character after) {
        final ArrayList<String> diffs = new ArrayList<>();

        for (Map.Entry<Feature, Integer> e : after.features.entrySet()) {
            final int b = before.features.get(e.getKey());
            final int a = e.getValue();
            if (b != a)
                diffs.add(L10n.fmt("aorta.diff.char.feature", e.getKey().tr(), b, a));
        }

        final MapDifference<String, BodyPart> bpDiff = Maps.difference(before.getBodyPartsMap(), after.getBodyPartsMap());
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

        if (before.psionics != after.psionics) {
            diffs.add(L10n.fmt("aorta.diff.char.psionics", before.psionics, after.psionics));
        }

        return String.join(", ", diffs);
    }

    static String diff(CharStatus before, CharStatus after) {
        final ArrayList<String> diffs = new ArrayList<>();

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

        for (Map.Entry<String, BodyPartHealth> e : after.health.entrySet()) {
            final BodyPartHealth b = before.health.get(e.getKey());
            final BodyPartHealth a = e.getValue();
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

    static String status(Character character, CharStatus before, CharStatus after) {
        final ArrayList<String> diffs = new ArrayList<>();

        if (!before.name.equalsIgnoreCase(after.name))
            diffs.add(L10n.fmt("aorta.diff.status.name", after.name));

        if (before.sanityLevel() != after.sanityLevel())
            diffs.add(L10n.fmt("aorta.diff.status.sanity_level", L10n.tr("aorta.diff.status.sanity." + after.sanityLevel())));

        final int vitalityBefore = before.vitalityLevel(character);
        final int vitalityAfter = after.vitalityLevel(character);
        if (vitalityAfter != vitalityBefore)
            diffs.add(L10n.fmt("aorta.diff.status.vitality_level", L10n.tr("aorta.diff.status.vitality." + vitalityAfter)));

        for (BodyPart bp : character.getBodyParts()) {
            final boolean bInjured = before.health.get(bp.name).isInjured(bp);
            final boolean aInjured = after.health.get(bp.name).isInjured(bp);
            if (aInjured && !bInjured)
                diffs.add(L10n.fmt("aorta.diff.status.injure", bp.name));
        }

        final int psionicsBefore = before.psionicsLevel(character);
        final int psionicsAfter = after.psionicsLevel(character);
        if (psionicsAfter != psionicsBefore) {
            if (psionicsAfter >= 4)
                diffs.add(L10n.tr("aorta.diff.status.psionics_critical"));
            else
                diffs.add(L10n.fmt("aorta.diff.status.psionics_level", psionicsAfter));
        }

        return String.join(", ", diffs);
    }
}
