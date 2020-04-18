package msifeed.mc.more.client.combat;

import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.scroll.ScrollArea;
import msifeed.mc.mellow.widgets.window.Window;
import msifeed.mc.more.crabs.action.Action;
import msifeed.mc.more.crabs.action.ActionHeader;
import msifeed.mc.more.crabs.action.ActionRegistry;
import msifeed.mc.more.crabs.action.effects.Effect;
import msifeed.mc.more.crabs.combat.ActionContext;
import msifeed.mc.more.crabs.combat.FighterInfo;
import msifeed.mc.more.crabs.rolls.Criticalness;
import msifeed.mc.more.crabs.rolls.Dices;
import msifeed.mc.more.crabs.utils.ActionAttribute;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.sys.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class TestCombatScreen extends MellowGuiScreen {
    final ScrollArea list = new ScrollArea();

    public TestCombatScreen() {

        final Window window = new Window();
        window.setTitle("Test combat tool");
        scene.addChild(window);

        final Widget content = window.getContent();
        content.setLayout(ListLayout.VERTICAL);

        list.setSizeHint(120, 150);
        content.addChild(list);

        refill();

        content.addChild(new Separator());

        final ButtonLabel reloadConfigBtn = new ButtonLabel("Reload config");
        reloadConfigBtn.setClickCallback(() -> {
            ConfigManager.reload();
            refill();
        });
        content.addChild(reloadConfigBtn);
    }

    private void refill() {
        list.clearChildren();
        ActionRegistry.getFullActions().stream()
                .sorted(ActionHeader::compareTo)
                .forEach(action -> {
                    final ButtonLabel btn = new ButtonLabel(action.getTitle());
                    btn.setClickCallback(() -> doAction(action));
                    list.addChild(btn);
                });
    }

    private static void doAction(Action action) {
        final EntityPlayer player = Minecraft.getMinecraft().thePlayer;


        CombatAttribute.INSTANCE.update(player, context -> context.action = action);
        ActionAttribute.INSTANCE.set(player, new ActionContext(action, action.isOffencive() ? ActionContext.Role.offence : ActionContext.Role.defence));
        final FighterInfo self = new FighterInfo(player);

        // Apply scores
        self.act.resetScore();

        self.act.scorePlayerMod = self.mod.roll;
        self.act.critical = Dices.critical();
        if (self.act.critical == Criticalness.FAIL)
            self.act.successful = false;

        send(player, String.format("%s - mod %d, crit: %s", action.getTitle(), self.act.scorePlayerMod, self.act.critical.toString()));

        for (Effect e : self.act.action.self) {
            if (e.shouldApply(Effect.Stage.SCORE, self.act, null)) {
                final int scoreBefore = self.act.scoreAction;
                e.apply(self, null);
                final int scoreDiff = self.act.scoreAction - scoreBefore;
                send(player, String.format("  %s -> %d (%+d)", e.toString(), self.act.scoreAction, scoreDiff));
            }
        }

//        applyEffects(self.act.action.self, Effect.Stage.SCORE, self, null);
//        applyBuffs(self.com.buffs, Effect.Stage.SCORE, self);


        send(player, String.format("result score %d", self.act.score()));

    }

    private static void send(EntityPlayer player, String text) {
        player.addChatMessage(new ChatComponentText(text));
    }
}
