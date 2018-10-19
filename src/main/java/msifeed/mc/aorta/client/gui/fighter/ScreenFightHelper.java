package msifeed.mc.aorta.client.gui.fighter;

import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.EntityLivingBase;

public class ScreenFightHelper extends MellowGuiScreen {
    final EntityLivingBase entity;

    public ScreenFightHelper(EntityLivingBase entity) {
        this.entity = entity;

        final Window window = new Window();
        window.setTitle("Fight Helper");
        scene.addChild(window);

        final Widget windowContent = window.getContent();

        windowContent.addChild(new BodypartHealthView(this));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
