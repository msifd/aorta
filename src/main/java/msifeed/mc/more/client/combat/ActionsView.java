package msifeed.mc.more.client.combat;

import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.scroll.ScrollArea;
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
        setSizeHint(100, 100);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
        setLayout(new GridLayout());

        refill();
    }

    public void refill() {
        CombatAttribute.get(entity).ifPresent(ctx -> {
            if (!ctx.phase.isInCombat())
                return;

            final ScrollArea list = new ScrollArea();
            list.setSizeHint(100, 150);
            addChild(list);

            final boolean defence = ctx.phase == CombatContext.Phase.DEFEND;

            final ActionTag incomingAttackType;
            if (defence) {
                final Entity foe = entity.worldObj.getEntityByID(ctx.target);
                if (foe == null)
                    return;
                final ActionHeader action = CombatAttribute.require(foe).action;
                if (action == null)
                    return;
                incomingAttackType = action.getType();
            } else {
                incomingAttackType = null;
            }

            ActionRegistry.getActionHeaders().stream()
                    .filter(action -> defence
                            ? ctx.acceptsDefendAction(incomingAttackType, action)
                            : ctx.acceptsOffendAction(action))
                    .sorted(ActionHeader::compareTo)
                    .forEach(action -> {
                        final ButtonLabel btn = new ButtonLabel(action.getTitle());
                        btn.setClickCallback(() -> doAction(action));
                        list.addChild(btn);
                    });
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
