package msifeed.mc.aorta.client.gui.roller;

import msifeed.mc.aorta.core.rules.FightAction;
import msifeed.mc.aorta.core.rules.RollRpc;
import msifeed.mc.aorta.rpc.Rpc;
import msifeed.mc.aorta.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
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

        final Widget attack = new Widget();
        attack.setLayout(new GridLayout());
        attack.addChild(makeActionButton(FightAction.HIT));
        attack.addChild(makeActionButton(FightAction.SPECIAL_HIT));
        attack.addChild(makeActionButton(FightAction.SHOT));
        attack.addChild(makeActionButton(FightAction.SPECIAL_SHOT));
        addChild(attack);

        addChild(new Separator());

        final Widget other = new Widget();
        other.setLayout(new GridLayout());
        other.addChild(makeActionButton(FightAction.SELF_USE));
        other.addChild(makeActionButton(FightAction.ENEMY_USE));
        other.addChild(makeActionButton(FightAction.BLOCK));
        other.addChild(makeActionButton(FightAction.DODGE));
        addChild(other);
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

        Rpc.sendToServer(RollRpc.rollAction, entity.getEntityId(), action, mod);
        ScreenRoller.lastRolled = System.currentTimeMillis();
    }
}
