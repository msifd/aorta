package msifeed.mc.more.client.combat;

import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.more.crabs.combat.CombatRpc;
import net.minecraft.entity.EntityLivingBase;

public class StageView extends Widget {
    public StageView(EntityLivingBase entity) {
        setLayout(ListLayout.VERTICAL);
        addButton("join", () -> CombatRpc.join(entity.getEntityId()));
        addButton("leave", () -> CombatRpc.leave(entity.getEntityId()));
        addButton("cancel", () -> CombatRpc.cancelAction(entity.getEntityId()));
        addButton("soft reset", () -> CombatRpc.reset(entity.getEntityId()));
//        addButton("hard reset", () -> CombatRpc.reset(entity.getEntityId()));
    }

    private void addButton(String label, Runnable callback) {
        final ButtonLabel btn = new ButtonLabel(label);
        btn.setClickCallback(callback);
        addChild(btn);
    }
}
