package msifeed.mc.mellow.mc;

import cpw.mods.fml.common.FMLCommonHandler;
import msifeed.mc.mellow.handlers.KeyHandler;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.render.RenderUtils;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.scene.ProfilingScene;
import msifeed.mc.mellow.widgets.scene.Scene;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class MellowGuiScreen extends GuiScreen {
    private static final Logger LOGGER = LogManager.getLogger("Mellow.GuiScreen");
    protected Scene scene = new ProfilingScene();

    public static RenderItem getRenderItem() {
        return itemRender;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        final ScaledResolution scale = RenderUtils.getScaledResolution();
        scene.getGeometry().set(0, 0, scale.getScaledWidth(), scale.getScaledHeight());
    }

    @Override
    public void drawScreen(int xMouse, int yMouse, float tick) {
        try {
            scene.update();
            scene.render();
            Widget.hoveredWidget = scene.lookupWidget(new Point(xMouse, yMouse)).findFirst().orElse(null);
        } catch (Exception e) {
            LOGGER.throwing(e);
            closeGui();
            return;
        }

        if (scene.getChildren().isEmpty())
            closeGui();
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();

        final int xMouse = Mouse.getEventX() * this.width / this.mc.displayWidth;
        final int yMouse = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        if (Mouse.hasWheel()) {
            final int dWheel = Mouse.getDWheel();
            if (dWheel != 0) {
                scene.lookupWidget(new Point(xMouse, yMouse))
                        .filter(w -> w instanceof MouseHandler.Wheel)
                        .findFirst()
                        .ifPresent(w -> ((MouseHandler.Wheel) w).onMouseWheel(xMouse, yMouse, dWheel));
            }
        }
    }

    @Override
    protected void mouseClicked(int xMouse, int yMouse, int button) {
        try {
            scene.lookupWidget(new Point(xMouse, yMouse))
                    .findFirst()
                    .ifPresent(w -> {
                        if (w instanceof MouseHandler.Press)
                            ((MouseHandler.Press) w).onPress(xMouse, yMouse, button);
                        Widget.setFocused(w);
                        Widget.pressedWidget = w;
                    });
        } catch (Exception e) {
            LOGGER.throwing(e);
        }
    }

    @Override
    protected void mouseClickMove(int xMouse, int yMouse, int button, long timeSinceMouseClick) {
        try {
            if (Widget.pressedWidget instanceof MouseHandler.Move)
                ((MouseHandler.Move) Widget.pressedWidget).onMove(xMouse, yMouse, button);
        } catch (Exception e) {
            LOGGER.throwing(e);
        }
    }

    @Override
    protected void mouseMovedOrUp(int xMouse, int yMouse, int button) {
        if (Widget.pressedWidget == null)
            return;

        try {
            final Widget pressedWidget = Widget.pressedWidget;
            if (button == -1) {
                if (pressedWidget instanceof MouseHandler.Move)
                    ((MouseHandler.Move) pressedWidget).onMove(xMouse, yMouse, button);
            } else {
                if (pressedWidget instanceof MouseHandler.Release)
                    ((MouseHandler.Release) pressedWidget).onRelease(xMouse, yMouse, button);

                scene.lookupWidget(new Point(xMouse, yMouse))
                        .filter(w -> w == pressedWidget)
                        .filter(w -> w instanceof MouseHandler.Click)
                        .findFirst()
                        .ifPresent(w -> ((MouseHandler.Click) w).onClick(xMouse, yMouse, button));

                Widget.pressedWidget = null;
            }
        } catch (Exception e) {
            LOGGER.throwing(e);
        }
    }

    @Override
    public void handleKeyboardInput() {
        Keyboard.enableRepeatEvents(true);
        if (!Keyboard.getEventKeyState())
            return;

        final char c = Keyboard.getEventCharacter();
        final int k = Keyboard.getEventKey();

        if (k == Keyboard.KEY_ESCAPE) {
            closeGui();
//            Widget focusedWindow = Widget.focusedWidget;
//
//            if (focusedWindow != null) {
//                while (focusedWindow != null && !(focusedWindow instanceof Window))
//                    focusedWindow = focusedWindow.getParent();
//            } else {
//                for (Widget w : scene.getChildren()) {
//                    if (w instanceof Window) {
//                        focusedWindow = w;
//                        break;
//                    }
//                }
//            }
//
//            if (focusedWindow != null) {
//                focusedWindow.getParent().removeChild(focusedWindow);
//                Widget.setFocused(null);
//            }
            return;
        }

        if (Widget.focusedWidget instanceof KeyHandler) {
            try {
                ((KeyHandler) Widget.focusedWidget).onKeyboard(c, k);
            } catch (Exception e) {
                LOGGER.throwing(e);
            }
        } else {
            FMLCommonHandler.instance().fireKeyInput();
        }
    }

    public void closeGui() {
        Minecraft.getMinecraft().displayGuiScreen(null);
    }
}
