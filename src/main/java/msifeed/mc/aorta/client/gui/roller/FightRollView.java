package msifeed.mc.aorta.client.gui.roller;

import msifeed.mc.aorta.core.rolls.FightAction;
import msifeed.mc.aorta.core.rolls.RollRpc;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import net.minecraft.entity.EntityLivingBase;

class FightRollView extends Widget {
    private final EntityLivingBase entity;
    private final TargetView targetView;

    FightRollView(EntityLivingBase entity, TargetView targetView) {
        this.entity = entity;
        this.targetView = targetView;

        setLayout(new GridLayout());

        for (FightAction action : FightAction.values())
            addChild(makeRollButton(action));
    }

    private Widget makeRollButton(FightAction action) {
        final ButtonLabel b = new ButtonLabel(action.tr());
        b.getSizeHint().x = 30;
        b.setClickCallback(() -> roll(action));
        return b;
    }

    private void roll(FightAction action) {
        if (System.currentTimeMillis() - ScreenRoller.prevRollTime < 1000)
            return;

        final String target = action.canTarget() ? targetView.getTarget() : "";
        RollRpc.rollAction(entity.getEntityId(), action, target);
        ScreenRoller.prevRollTime = System.currentTimeMillis();
    }
}
