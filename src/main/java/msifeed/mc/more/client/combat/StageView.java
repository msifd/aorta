package msifeed.mc.more.client.combat;

import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.combat.CombatRpc;
import msifeed.mc.more.crabs.meta.MetaInfo;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Optional;

public class StageView extends Widget {
    public StageView(EntityLivingBase entity) {
        setLayout(ListLayout.VERTICAL);

        CombatAttribute.get(entity).ifPresent(context -> {
            if (context.phase.isInCombat())
                addButton("leave", () -> CombatRpc.leave(entity.getEntityId()));
            else
                addButton("join", () -> CombatRpc.join(entity.getEntityId()));
            addButton("soft reset", () -> CombatRpc.reset(entity.getEntityId()));
        });

        final Optional<Character> charOpt = CharacterAttribute.get(entity);
        final Optional<MetaInfo> metaOpt = MetaAttribute.get(entity);
        if (!charOpt.isPresent() || !metaOpt.isPresent()) {
            addButton("Recruit", () -> CombatRpc.recruitEntity(entity.getEntityId()));
        } else if (!(entity instanceof EntityPlayer)) {
            addButton("Dismiss", () -> CombatRpc.dismissEntity(entity.getEntityId()));
        }
    }

    private void addButton(String label, Runnable callback) {
        final ButtonLabel btn = new ButtonLabel(label);
        btn.setClickCallback(callback);
        addChild(btn);
    }
}
