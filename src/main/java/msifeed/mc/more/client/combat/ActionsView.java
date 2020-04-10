package msifeed.mc.more.client.combat;

import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonMultiLabel;
import msifeed.mc.mellow.widgets.scroll.ScrollArea;
import msifeed.mc.more.crabs.action.ActionHeader;
import msifeed.mc.more.crabs.action.ActionRegistry;
import msifeed.mc.more.crabs.action.ActionTag;
import msifeed.mc.more.crabs.action.Combo;
import msifeed.mc.more.crabs.combat.CombatContext;
import msifeed.mc.more.crabs.combat.CombatRpc;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.util.HashMap;

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
            list.setSizeHint(120, 150);
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

            final HashMap<String, String> availableCombos = new HashMap<>();
            for (ActionHeader header : ActionRegistry.getActionHeaders()) {
                final Combo.ComboLookup combo = Combo.find(ActionRegistry.getCombos(), ctx.prevActions, header.id);
                if (combo != null)
                    availableCombos.put(header.id, combo.c.action.getTitle());
            }

            ActionRegistry.getActionHeaders().stream()
                    .filter(action -> defence
                            ? action.isValidDefencive(incomingAttackType)
                            : action.isOffencive())
                    .sorted(ActionHeader::compareTo)
                    .forEach(action -> {
                        final ButtonMultiLabel btn = new ButtonMultiLabel(action.getTitle());
                        final String comboTitle = availableCombos.get(action.getId());
                        if (comboTitle != null)
                            btn.addLabel(" + " + comboTitle);

                        btn.setDisabled(ctx.phase != CombatContext.Phase.IDLE && ctx.phase != CombatContext.Phase.DEFEND);
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
