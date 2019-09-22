package msifeed.mc.aorta.core.rolls;

import msifeed.mc.aorta.sys.utils.L10n;

public enum FightAction {
    HIT, SPECIAL_HIT, SHOT, SPECIAL_SHOT,
    SELF_USE, ENEMY_USE,
    BLOCK, DODGE;

    public boolean canTarget() {
        switch (this) {
            case HIT:
            case SPECIAL_HIT:
            case SHOT:
            case SPECIAL_SHOT:
            case ENEMY_USE:
                return true;
            default:
                return false;
        }
    }

    public String tr() {
        return L10n.tr("aorta.action." + name().toLowerCase());
    }
}
