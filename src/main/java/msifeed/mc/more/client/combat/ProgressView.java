package msifeed.mc.more.client.combat;

import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.FlatButtonLabel;
import msifeed.mc.mellow.widgets.text.WordwrapLabel;
import msifeed.mc.more.crabs.action.ActionHeader;
import msifeed.mc.more.crabs.action.ActionRegistry;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.combat.CombatContext;
import msifeed.mc.more.crabs.combat.CombatRpc;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Optional;
import java.util.stream.Collectors;

public class ProgressView extends Widget {
    private final EntityLivingBase entity;

    ProgressView(EntityLivingBase entity) {
        this.entity = entity;
//
//        getSizeHint().x = 110;
//        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.PREFERRED);

        getMargin().top = 1;
        setLayout(new ListLayout(ListLayout.Direction.VERTICAL, 2));

        refill();
    }

    private void refill() {
        clearChildren();

        final Optional<CombatContext> optCom = CombatAttribute.get(entity);
        if (optCom.isPresent())
            refillHasContext(optCom.get());
        else
            refillWithoutContext();
    }

    private void refillHasContext(CombatContext context) {
        switch (context.phase) {
            case NONE:
                addButton("more.gui.combat.join", () -> CombatRpc.join(entity.getEntityId()));
                break;
            case IDLE:
                if (context.action == null)
                    addPane("more.gui.combat.tips.offence_action");
                else {
                    addPane("more.gui.combat.tips.current_action", context.action.getTitle());
                    if (context.action.requiresNoRoll())
                        addPane("more.gui.combat.tips.confirm_action");
                    else
                        addPane("more.gui.combat.tips.damage_target");
                    addPane("more.gui.combat.tips.change_action");
                }
                break;
            case ATTACK:
                addPane("more.gui.combat.tips.current_action", context.action.getTitle());
                addPane("more.gui.combat.tips.damage_target");
                addButton("more.gui.combat.end_attack", () -> CombatRpc.endAttack(entity.getEntityId()));
                break;
            case WAIT:
                addPane("more.gui.combat.tips.wait_defender");
                break;
            case DEFEND:
                if (context.action == null)
                    addPane("more.gui.combat.tips.defence_action");
                else {
                    addPane("more.gui.combat.tips.current_action", context.action.getTitle());
                    addPane("more.gui.combat.tips.confirm_action");
                    addPane("more.gui.combat.tips.change_action");
                }
                break;
            case END:
                break;
            case LEAVE:
                addPane("more.gui.combat.tips.leaving");
                break;
        }

        if (!context.buffs.isEmpty()) {
            final String buffs = context.buffs.stream()
                    .map(Buff::toString)
                    .collect(Collectors.joining("\n"));
            addPane("more.gui.combat.tips.buffs", buffs);
        }

        if (!context.prevActions.isEmpty()) {
            final String actions = context.prevActions.stream()
                    .map(ActionRegistry::getHeader)
                    .map(ActionHeader::getTitle)
                    .collect(Collectors.joining(", "));
            addPane("more.gui.combat.tips.prev_actions", actions);
        }

        if (context.phase.isInCombat()) {
            addButton("more.gui.combat.leave", () -> CombatRpc.leave(entity.getEntityId()));
            addButton("more.gui.combat.reset", () -> CombatRpc.reset(entity.getEntityId()));

            if (!(entity instanceof EntityPlayer))
                addButton("more.gui.combat.dismiss", () -> CombatRpc.dismissEntity(entity.getEntityId()));
        }
    }

    private void refillWithoutContext() {
        if (!(entity instanceof EntityPlayer))
            addButton("more.gui.combat.recruit", () -> CombatRpc.recruitEntity(entity.getEntityId()));
    }

    private void addButton(String trKey, Runnable callback) {
        final FlatButtonLabel btn = new FlatButtonLabel(L10n.tr(trKey));
        btn.setClickCallback(callback);
        addChild(btn);
    }

    private void addPane(String fmtKey, Object... args) {
        addChild(new Pane(L10n.fmt(fmtKey, args), 140));
    }

    private static class Pane extends Widget {
        Pane(String text, int width) {
            getSizeHint().x = width;
            getMargin().set(1, 2, 2, 2);
            setLayout(new AnchorLayout());

            final WordwrapLabel label = new WordwrapLabel();
            label.getSizeHint().x = getSizeHint().x;
            label.setText(text);
            addChild(label);
        }

        @Override
        protected void renderSelf() {
            RenderShapes.frame(getGeometry(), 2, 0xb3937b);
        }
    }

}
