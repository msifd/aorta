package msifeed.mc.mellow.mc;

import msifeed.mc.mellow.MellowGlobal;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.handlers.MouseHandler;
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
        final Widget lookup = scene.lookupWidget(new Point3f(xMouse, yMouse, 0));
        MellowGlobal.hoveredWidget = lookup;

        scene.render();
    }

    @Override
    protected void mouseClicked(int xMouse, int yMouse, int button) {
        final Widget lookup = scene.lookupWidget(new Point3f(xMouse, yMouse, 0));
        if (lookup instanceof MouseHandler.Press)
            ((MouseHandler.Press) lookup).onPress(xMouse, yMouse, button);
        MellowGlobal.focusedWidget = MellowGlobal.pressedWidget = lookup;
    }

    @Override
    protected void mouseClickMove(int xMouse, int yMouse, int button, long timeSinceMouseClick) {
        final Widget lookup = scene.lookupWidget(new Point3f(xMouse, yMouse, 0));
        if (lookup instanceof MouseHandler.Move)
            ((MouseHandler.Move) lookup).onMove(xMouse, yMouse, button);
    }

    @Override
    protected void mouseMovedOrUp(int xMouse, int yMouse, int button) {
        final Widget lookup = scene.lookupWidget(new Point3f(xMouse, yMouse, 0));
        if (lookup != null) {
            if (button == -1)
                if (lookup instanceof MouseHandler.Move)
                    ((MouseHandler.Move) lookup).onMove(xMouse, yMouse, button);
            else if (lookup instanceof MouseHandler.Release)
                ((MouseHandler.Release) lookup).onRelease(xMouse, yMouse, button);

            if (MellowGlobal.pressedWidget == lookup && lookup instanceof MouseHandler.Click)
                ((MouseHandler.Click) lookup).onClick(xMouse, yMouse, button);
        }
    }

    @Override
    protected void keyTyped(char c, int key) {
        super.keyTyped(c, key);
    }
}
