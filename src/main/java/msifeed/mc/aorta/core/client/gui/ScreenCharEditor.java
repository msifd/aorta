package msifeed.mc.aorta.core.client.gui;

import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Window;
import net.minecraft.entity.EntityLivingBase;

public class ScreenCharEditor extends MellowGuiScreen {
    private final EntityLivingBase entity;

    public ScreenCharEditor(EntityLivingBase entity) {
        this.entity = entity;
        scene.addChild(new Window());
    }
}
