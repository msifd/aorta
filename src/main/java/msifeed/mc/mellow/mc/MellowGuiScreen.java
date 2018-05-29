package msifeed.mc.mellow.mc;

import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.widgets.Scene;
import net.minecraft.client.gui.GuiScreen;

import javax.vecmath.Point3f;
import java.util.Optional;

public class MellowGuiScreen extends GuiScreen {
    protected Scene scene = new Scene();

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int xMouse, int yMouse, float tick) {
        Widget.hoveredWidget = scene.lookupWidget(new Point(xMouse, yMouse)).orElse(null);

        scene.update();
        scene.render();
    }

    @Override
    protected void mouseClicked(int xMouse, int yMouse, int button) {
        final Widget lookup = scene.lookupWidget(new Point(xMouse, yMouse)).orElse(null);
        if (lookup instanceof MouseHandler.Press)
            ((MouseHandler.Press) lookup).onPress(xMouse, yMouse, button);
        Widget.focusedWidget = Widget.pressedWidget = lookup;
    }

    @Override
    protected void mouseClickMove(int xMouse, int yMouse, int button, long timeSinceMouseClick) {
        scene.lookupWidget(new Point(xMouse, yMouse)).ifPresent(widget -> {
            if (widget instanceof MouseHandler.Move)
                ((MouseHandler.Move) widget).onMove(xMouse, yMouse, button);
        });
    }

    @Override
    protected void mouseMovedOrUp(int xMouse, int yMouse, int button) {
        scene.lookupWidget(new Point(xMouse, yMouse)).ifPresent(widget -> {
            if (button == -1)
                if (widget instanceof MouseHandler.Move)
                    ((MouseHandler.Move) widget).onMove(xMouse, yMouse, button);
                else if (widget instanceof MouseHandler.Release)
                    ((MouseHandler.Release) widget).onRelease(xMouse, yMouse, button);

            if (Widget.pressedWidget == widget && widget instanceof MouseHandler.Click)
                ((MouseHandler.Click) widget).onClick(xMouse, yMouse, button);
        });
    }

    @Override
    protected void keyTyped(char c, int key) {
        super.keyTyped(c, key);
    }
}
