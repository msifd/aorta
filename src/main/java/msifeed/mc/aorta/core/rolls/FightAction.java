package msifeed.mc.aorta.core.rolls;

import msifeed.mc.aorta.sys.utils.L10n;

public enum FightAction {
    HIT, SPECIAL_HIT, SHOT, SPECIAL_SHOT,
    SELF_USE, ENEMY_USE,
    BLOCK, DODGE,
    PSIONIC;

    public String tr() {
        return L10n.tr("aorta.action." + name().toLowerCase());
    }
}
