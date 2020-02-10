package msifeed.mc.more.crabs.combat.action;

import msifeed.mc.sys.utils.L10n;

public enum ActionType {
    MELEE, RANGED, MAGIC, SUPPORT, DEFENCE, PASSIVE;

    public boolean defencive() {
        return this == DEFENCE || this == PASSIVE;
    }

    public String pretty() {
        return L10n.tr("misca.crabs.action_type." + toString());
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
