package msifeed.mc.more.client.combat;

import joptsimple.internal.Strings;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.text.WordwrapLabel;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.combat.CombatContext;
import msifeed.mc.more.crabs.combat.CombatRpc;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import net.minecraft.entity.EntityLivingBase;

import java.util.ArrayList;
import java.util.Optional;

public class ProgressView extends Widget {
    private final EntityLivingBase entity;

    ProgressView(EntityLivingBase entity) {
        this.entity = entity;

        getSizeHint().x = 110;
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.PREFERRED);
//        setSizeHint(110, 0);
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
                addPane("Join combat");
                break;
            case IDLE:
                if (context.action == null)
                    addPane("Select offence action");
                else {
                    addPane("Current action: " + context.action.title);
                    if (context.action.requiresNoRoll())
                        addPane("Select offence action again to confirm");
                    else
                        addPane("Damage your enemy");
                    addPane("You can change action");
                }
                break;
            case ATTACK:
                addPane("Damage your enemy");
                final ButtonLabel endBtn = new ButtonLabel("End attack");
                endBtn.setClickCallback(() -> CombatRpc.endAttack(entity.getEntityId()));
                addChild(endBtn);
                break;
            case WAIT:
                addPane("Wait for your enemy action");
                break;
            case DEFEND:
                if (context.action == null)
                    addPane("Select defence action");
                else {
                    addPane("Current action: " + context.action.title);
                    addPane("Select defence action again to confirm");
                    addPane("You can change action");
                }
                break;
            case END:
                addPane("Stay calm...");
                break;
            case LEAVE:
                addPane("You are leaving");
                break;
        }

        if (!context.buffs.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Buffs:\n");
            for (Buff b : context.buffs) {
                sb.append(b.toString());
                sb.append('\n');
            }
            sb.deleteCharAt(sb.length() - 1);
            addPane(sb.toString());
        }

        if (!context.prevActions.isEmpty()) {
            addPane("Prev. actions:\n" + Strings.join(new ArrayList<>(context.prevActions), ","));
        }
    }

    private void refillWithoutContext() {
        addPane("Not a combatant!");
    }

    private void addPane(String text) {
        addChild(new Pane(text, 100));
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
