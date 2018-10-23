package msifeed.mc.aorta.client.gui.roller;

import msifeed.mc.aorta.core.net.RollRequests;
import msifeed.mc.aorta.core.rules.FightAction;
import msifeed.mc.aorta.utils.L10n;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import net.minecraft.entity.EntityLivingBase;

class FightRollView extends Widget {
    private final EntityLivingBase entity;

    FightRollView(EntityLivingBase entity) {
        this.entity = entity;
        setLayout(ListLayout.VERTICAL);

        addChild(makeActionButton(FightAction.HIT));
        addChild(makeActionButton(FightAction.SPECIAL_HIT));
        addChild(makeActionButton(FightAction.SHOT));
        addChild(makeActionButton(FightAction.SPECIAL_SHOT));
        addChild(new Separator());
        addChild(makeActionButton(FightAction.SELF_USE));
        addChild(makeActionButton(FightAction.ENEMY_USE));
        addChild(new Separator());
        addChild(makeActionButton(FightAction.BLOCK));
        addChild(makeActionButton(FightAction.DODGE));
    }

    private Widget makeActionButton(FightAction action) {
        final String s = L10n.tr("aorta.action." + action.name().toLowerCase());
        final ButtonLabel b = new ButtonLabel(s);
        b.setClickCallback(() -> roll(action, ScreenRoller.lastModifier));
        return b;
    }

    private void roll(FightAction action, int mod) {
        if (System.currentTimeMillis() - ScreenRoller.lastRolled < 1000)
            return;

        RollRequests.rollAction(entity, action, mod);
        ScreenRoller.lastRolled = System.currentTimeMillis();
    }
}
