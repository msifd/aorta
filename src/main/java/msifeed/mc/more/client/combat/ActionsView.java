package msifeed.mc.more.client.combat;

import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.more.crabs.action.ActionHeader;
import msifeed.mc.more.crabs.action.ActionRegistry;
import msifeed.mc.more.crabs.action.ActionTag;
import msifeed.mc.more.crabs.combat.CombatContext;
import msifeed.mc.more.crabs.combat.CombatRpc;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ActionsView extends Widget {
    private final EntityLivingBase entity;
    private long prevActionTime = 0;

    public ActionsView(EntityLivingBase entity) {
        this.entity = entity;

        setLayout(new GridLayout());
        refill();
    }

    public void refill() {
        CombatAttribute.get(entity).ifPresent(ctx -> {
            final ActionTag incomingActionType;
            if (ctx.stage == CombatContext.Stage.DEFEND) {
                final Entity foe = entity.worldObj.getEntityByID(ctx.target);
                if (foe == null)
                    return;
                final ActionHeader action = CombatAttribute.require(foe).action;
                if (action == null)
                    return;
                incomingActionType = action.getType();
            } else {
                incomingActionType = null;
            }

            for (ActionHeader action : ActionRegistry.getActionHeaders()) {
                if (ctx.discardAction(action))
                    continue;
                if (incomingActionType != null && incomingActionType != action.getType())
                    continue;
                final ButtonLabel btn = new ButtonLabel(String.format("%s - '%s'", action.id, action.title));
                btn.setClickCallback(() -> doAction(action));
                addChild(btn);
            }
        });
    }

    private void doAction(ActionHeader action) {
        if (System.currentTimeMillis() - prevActionTime < 1000)
            return;

//        final String target = action.canTarget() ? targetView.getTarget() : "";
//        RollRpc.rollAction(entity.getEntityId(), action, target);

        CombatRpc.doAction(entity.getEntityId(), action.id);
        prevActionTime = System.currentTimeMillis();
    }
}
