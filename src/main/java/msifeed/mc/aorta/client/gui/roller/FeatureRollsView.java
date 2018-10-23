package msifeed.mc.aorta.client.gui.roller;

import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.net.RollRequests;
import msifeed.mc.aorta.utils.L10n;
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
            final String s = L10n.tr("aorta.feature." + f.name().toLowerCase());
            final ButtonLabel b = new ButtonLabel(s);
            b.setClickCallback(() -> roll(f, ScreenRoller.lastModifier));
            addChild(b);
        }
    }

    private void roll(Feature feature, int mod) {
        if (System.currentTimeMillis() - ScreenRoller.lastRolled < 1000)
            return;

        RollRequests.rollFeature(entity, feature, mod);
        ScreenRoller.lastRolled = System.currentTimeMillis();
    }
}
