package msifeed.mc.aorta.client.gui.roller;

import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.rolls.RollRpc;
import msifeed.mc.aorta.rpc.Rpc;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import net.minecraft.entity.EntityLivingBase;

class FeatureRollsView extends Widget {
    private final EntityLivingBase entity;

    FeatureRollsView(EntityLivingBase entity) {
        this.entity = entity;
        setLayout(new GridLayout());

        for (Feature f : Feature.values()) {
            final ButtonLabel b = new ButtonLabel(f.tr());
            b.setClickCallback(() -> roll(f));
            addChild(b);
        }

        for (Feature.ComplexFeature cf : Feature.ComplexFeature.values()) {
            final ButtonLabel b = new ButtonLabel(cf.tr());
            b.setClickCallback(() -> roll(cf.feats));
            addChild(b);
        }
    }

    private void roll(Feature... features) {
        if (System.currentTimeMillis() - ScreenRoller.prevRollTime < 1000)
            return;

        Rpc.sendToServer(RollRpc.rollFeature, entity.getEntityId(), features);
        ScreenRoller.prevRollTime = System.currentTimeMillis();
    }
}
