package msifeed.mc.aorta.core.character;

import msifeed.mc.aorta.utils.L10n;

import java.util.ArrayList;
import java.util.Map;

class Differ {
    static String diff(Character before, Character after) {
        return "";
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
                diffs.add(L10n.fmt(
                        b.health < a.health ? "aorta.diff.status.add_health" : "aorta.diff.status.rem_health",
                        e.getKey(), n, trPoints(n)
                ));
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
}
