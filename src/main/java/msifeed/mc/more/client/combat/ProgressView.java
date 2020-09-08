package msifeed.mc.more.client.combat;

import msifeed.mc.commons.traits.Trait;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.FillLayout;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.FlatButtonLabel;
import msifeed.mc.mellow.widgets.scroll.ScrollArea;
import msifeed.mc.mellow.widgets.text.WordwrapLabel;
import msifeed.mc.more.crabs.action.ActionHeader;
import msifeed.mc.more.crabs.action.ActionRegistry;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.action.effects.Effect;
import msifeed.mc.more.crabs.combat.CombatContext;
import msifeed.mc.more.crabs.combat.CombatRpc;
import msifeed.mc.more.crabs.combat.PotionsHandler;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.more.crabs.utils.GetUtils;
import msifeed.mc.sys.utils.ChatUtils;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProgressView extends Widget {
    private final EntityLivingBase entity;
    private final ScrollArea scroll = new ScrollArea();

    ProgressView(EntityLivingBase entity) {
        this.entity = entity;

        setSizeHint(150, 150);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.PREFERRED);
        setLayout(FillLayout.INSTANCE);

        scroll.getMargin().set(1, 0, 0, 1);
        scroll.setSpacing(2);
        addChild(scroll);

        refill();
    }

    public void refill() {
        scroll.clearChildren();

        final Optional<CombatContext> optCom = CombatAttribute.get(entity);
        try {
            if (optCom.isPresent())
                refillHasContext(optCom.get());
            else
                refillWithoutContext();
        } catch (Exception e) {
            CombatRpc.reset(entity.getEntityId());
        }
    }

    private void refillHasContext(CombatContext context) {
        if (context.isTraining())
            addPane("more.gui.combat.tips.training");

        switch (context.phase) {
            case NONE:
                addButton("more.gui.combat.join", () -> CombatRpc.join(entity.getEntityId()));
                addButton("more.gui.combat.training", () -> CombatRpc.training(entity.getEntityId()));
                return; // Stop here
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
                addPane("more.gui.combat.tips.defenders",
                        context.defenders.stream()
                                .map(id -> GetUtils.entityLiving(entity, id).orElse(null))
                                .filter(Objects::nonNull)
                                .map(ChatUtils::getPrettyName)
                                .collect(Collectors.joining("\n"))
                );

                addButton("more.gui.combat.end_attack", () -> CombatRpc.endAttack(entity.getEntityId()));
                break;
            case WAIT:
                final CombatContext offenderCom;
                if (context.role == CombatContext.Role.DEFENCE) {
                    offenderCom = GetUtils.entityLiving(entity, context.offender)
                            .flatMap(CombatAttribute::get)
                            .orElseThrow(RuntimeException::new);
                } else {
                    offenderCom = context;
                }
                addPane("more.gui.combat.tips.wait_defenders",
                        offenderCom.defenders.stream()
                                .map(id -> GetUtils.entityLiving(entity, id).orElse(null))
                                .filter(Objects::nonNull)
                                .map(ChatUtils::getPrettyName)
                                .collect(Collectors.joining("\n"))
                );
                break;
            case DEFEND:
                final EntityLivingBase offender = GetUtils.entityLiving(entity, context.offender)
                        .orElseThrow(RuntimeException::new);
                final ActionHeader action = CombatAttribute.get(offender)
                        .map(c -> c.action)
                        .orElseThrow(RuntimeException::new);

                addPane("more.gui.combat.tips.offender", ChatUtils.getPrettyName(offender));
                addPane("more.gui.combat.tips.enemy_action", action.getTitle());

                final double damageDealt = context.damageDealt.stream().mapToDouble(da -> da.amount).sum();
                if (damageDealt > 0)
                    addPane("more.gui.combat.tips.damage_dealt", damageDealt);
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

        final List<Effect> passiveBuffs = PotionsHandler.convertPassiveEffects(entity);
        final String buffList = Stream.concat(
                    context.buffs.stream().map(ProgressView::formatBuff),
                    passiveBuffs.stream().map(ProgressView::formatPassiveBuff))
                .collect(Collectors.joining("\n"));
        if (!buffList.isEmpty())
            addPane("more.gui.combat.tips.buffs", buffList);

        if (!context.prevActions.isEmpty()) {
            final String actions = context.prevActions.stream()
                    .map(ActionRegistry::getHeader)
                    .filter(Objects::nonNull)
                    .map(ActionHeader::getTitle)
                    .collect(Collectors.joining(", "));
            addPane("more.gui.combat.tips.prev_actions", actions);
        }

        if (context.phase.isInCombat()) {
            scroll.addChild(new Separator());
            addButton("more.gui.combat.reset", () -> CombatRpc.reset(entity.getEntityId()));
            addButton("more.gui.combat.leave", () -> CombatRpc.leave(entity.getEntityId()));

            final EntityPlayer self = Minecraft.getMinecraft().thePlayer;
            final boolean canControl = CharacterAttribute.hasAny(self, Trait.gm, Trait.__admin);
            if (canControl) {
                final CombatContext selfCom = CombatAttribute.require(self);
                if (selfCom.puppet != entity.getEntityId() && self.getEntityId() != entity.getEntityId()) {
                    addButton("more.gui.combat.control", () -> CombatRpc.setPuppet(entity.getEntityId()));
                } else if (selfCom.puppet != 0) {
                    addButton("more.gui.combat.end_control", () -> CombatRpc.setPuppet(0));
                }
            }

            if (!(entity instanceof EntityPlayer))
                addButton("more.gui.combat.rem_combat", () -> CombatRpc.removeCombatFromEntity(entity.getEntityId()));
        }
    }

    private void refillWithoutContext() {
        if (!(entity instanceof EntityPlayer))
            addButton("more.gui.combat.add_combat", () -> CombatRpc.addCombatToEntity(entity.getEntityId()));
    }

    private void addButton(String trKey, Runnable callback) {
        final FlatButtonLabel btn = new FlatButtonLabel(L10n.tr(trKey));
        btn.setClickCallback(callback);
        scroll.addChild(btn);
    }

    private void addPane(String fmtKey, Object... args) {
        scroll.addChild(new Pane(L10n.fmt(fmtKey, args), 140));
    }

    private static String formatBuff(Buff b) {
        final int counter = b.pause > 0 ? -b.pause : b.steps;
        return String.format(" %2d - %s", counter, b.effect.encode());
    }

    private static String formatPassiveBuff(Effect e) {
        return " ** - " + e.encode();
    }

    private static class Pane extends Widget {
        Pane(String text, int width) {
            getSizeHint().x = width;
            getMargin().set(1, 2, 2, 2);
            setLayout(new AnchorLayout());

            final WordwrapLabel label = new WordwrapLabel();
            label.getSizeHint().x = getSizeHint().x - getMargin().horizontal();
            label.setText(text);
            addChild(label);
        }

        @Override
        protected void renderSelf() {
            RenderShapes.frame(getGeometry(), 2, 0xb3937b);
        }
    }

}
