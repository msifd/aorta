package msifeed.mc.more.client.status;

import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.more.crabs.combat.CombatContext;
import msifeed.mc.more.crabs.combat.CombatRpc;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

class CombatGmView extends Widget {
    final EntityLivingBase entity;

    CombatGmView(EntityLivingBase entity) {
        this.entity = entity;
        setLayout(ListLayout.VERTICAL);
        refill();
    }

    public void refill() {
        final EntityPlayer self = Minecraft.getMinecraft().thePlayer;
        final CombatContext selfCtx = CombatAttribute.require(entity);

        clearChildren();

        if (entity != self) {
            if (selfCtx.puppet == 0) {
                final ButtonLabel setPuppetBtn = new ButtonLabel("Set puppet");
                setPuppetBtn.setClickCallback(() -> {
                    CombatRpc.setPuppet(entity.getEntityId());
                    refill();
                });
                addChild(setPuppetBtn);
            } else {
                final ButtonLabel resetPuppetBtn = new ButtonLabel("Reset puppet");
                resetPuppetBtn.setClickCallback(() -> {
                    CombatRpc.setPuppet(0);
                    refill();
                });
                addChild(resetPuppetBtn);
            }
        }
    }
}
