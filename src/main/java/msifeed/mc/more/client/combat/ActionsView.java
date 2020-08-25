package msifeed.mc.more.client.combat;

import msifeed.mc.mellow.layout.FillLayout;
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
import msifeed.mc.more.crabs.utils.GetUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class ActionsView extends Widget {
    private final EntityLivingBase entity;
    private long prevActionTime = 0;
    private double prevScrollPercent = 0;

    private final ScrollArea scroll = new ScrollArea();

    public ActionsView(EntityLivingBase entity) {
        this.entity = entity;
        setSizeHint(100, 150);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
        setLayout(FillLayout.INSTANCE);

        addChild(scroll);

        refill();
    }

    public void refill() {
        scroll.clearChildren();

        CombatAttribute.get(entity).ifPresent(ctx -> {
            if (!ctx.phase.isInCombat())
                return;

            final boolean defence = ctx.phase == CombatContext.Phase.DEFEND;

            final ActionTag incomingAttackType;
            if (defence) {
                incomingAttackType = GetUtils.entityLiving(entity, ctx.offender)
                        .flatMap(CombatAttribute::get)
                        .map(act -> act.action)
                        .map(ActionHeader::getType)
                        .orElse(null);
                if (incomingAttackType == null)
                    return;
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
                    .filter(a -> !a.hasAnyTag(ActionTag.hidden))
                    .filter(action -> defence
                            ? action.isValidDefencive(incomingAttackType)
                            : action.isOffencive())
                    .filter(this::actionSelfCheck)
                    .sorted(ActionHeader::compareTo)
                    .forEach(action -> {
                        final ButtonMultiLabel btn = new ButtonMultiLabel(action.getTitle());
                        final String comboTitle = availableCombos.get(action.getId());
                        if (comboTitle != null)
                            btn.addLabel(" + " + comboTitle);

                        btn.setDisabled(ctx.phase != CombatContext.Phase.IDLE && ctx.phase != CombatContext.Phase.DEFEND);
                        btn.setClickCallback(() -> doAction(action));
                        scroll.addChild(btn);
                    });
        });
    }

    private boolean actionSelfCheck(ActionHeader action) {
        if (action.requiresNoRoll() && action.hasAnyTag(ActionTag.apply)) {
            // Solo apply actions requires eatable or drinkable item in hand
            final ItemStack held = entity.getHeldItem();
            if (held == null || !(held.getItemUseAction() == EnumAction.eat || held.getItemUseAction() == EnumAction.drink))
                return false;
        }

        return true;
    }

    private void doAction(ActionHeader action) {
        if (System.currentTimeMillis() - prevActionTime < 500)
            return;

        CombatRpc.doAction(entity.getEntityId(), action.id);
        prevActionTime = System.currentTimeMillis();
    }
}
