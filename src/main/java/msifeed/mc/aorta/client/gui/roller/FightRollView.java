package msifeed.mc.aorta.client.gui.roller;

import msifeed.mc.aorta.core.rolls.FightAction;
import msifeed.mc.aorta.core.rolls.RollRpc;
import msifeed.mc.aorta.rpc.Rpc;
import msifeed.mc.aorta.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import net.minecraft.entity.EntityLivingBase;

class FightRollView extends Widget {
    private final EntityLivingBase entity;

    FightRollView(EntityLivingBase entity) {
        this.entity = entity;
        setLayout(ListLayout.VERTICAL);

        final Widget actions = new Widget();
        actions.setLayout(new GridLayout());
        actions.addChild(makeActionButton(FightAction.HIT));
        actions.addChild(makeActionButton(FightAction.SPECIAL_HIT));
        actions.addChild(makeActionButton(FightAction.SHOT));
        actions.addChild(makeActionButton(FightAction.SPECIAL_SHOT));
        actions.addChild(makeActionButton(FightAction.SELF_USE));
        actions.addChild(makeActionButton(FightAction.ENEMY_USE));
        actions.addChild(makeActionButton(FightAction.BLOCK));
        actions.addChild(makeActionButton(FightAction.DODGE));
        addChild(actions);
    }

    private Widget makeActionButton(FightAction action) {
        final String s = L10n.tr("aorta.action." + action.name().toLowerCase());
        final ButtonLabel b = new ButtonLabel(s);
        b.setClickCallback(() -> roll(action));
        return b;
    }

    private void roll(FightAction action) {
        if (System.currentTimeMillis() - ScreenRoller.prevRollTime < 1000)
            return;

        Rpc.sendToServer(RollRpc.rollAction, entity.getEntityId(), action);
        ScreenRoller.prevRollTime = System.currentTimeMillis();
    }
}
