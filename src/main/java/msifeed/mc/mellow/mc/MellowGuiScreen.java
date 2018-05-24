package msifeed.mc.mellow.mc;

import msifeed.mc.mellow.Widget;
import msifeed.mc.mellow.events.MouseEvent;
import msifeed.mc.mellow.widgets.Scene;
import net.minecraft.client.gui.GuiScreen;

import javax.vecmath.Point3f;

public class MellowGuiScreen extends GuiScreen {
    protected Scene scene = new Scene();

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int xMouse, int yMouse, float tick) {
        scene.render();
        final Widget lookup = scene.lookupWidget(new Point3f(xMouse, yMouse, 0));
        if (lookup != null)
            lookup.renderDebug();
    }

    @Override
    protected void mouseClicked(int xMouse, int yMouse, int button) {
        final Widget lookup = scene.lookupWidget(new Point3f(xMouse, yMouse, 0));
        if (lookup != null)
            scene.getEventBus().post(new MouseEvent.Click(lookup, xMouse, yMouse, button));
    }

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
        super.keyTyped(p_73869_1_, p_73869_2_);
    }
}
