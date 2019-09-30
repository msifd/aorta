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
        final Minecraft mc = Minecraft.getMinecraft();
        final ScaledResolution scale = RenderUtils.getScaledResolution();
        scene.getGeometry().set(0, 0, scale.getScaledWidth(), scale.getScaledHeight());
    }

    @Override
    public void drawScreen(int xMouse, int yMouse, float tick) {
        try {
            scene.update();
            scene.render();
            Widget.hoveredWidget = scene.lookupWidget(new Point(xMouse, yMouse)).orElse(null);
        } catch (Exception e) {
            LOGGER.throwing(e);
            closeGui();
            return;
        }

        if (scene.getChildren().isEmpty())
            closeGui();
    }

    @Override
    protected void mouseClicked(int xMouse, int yMouse, int button) {
        try {
            final Widget lookup = scene.lookupWidget(new Point(xMouse, yMouse)).orElse(null);
            if (lookup instanceof MouseHandler.Press)
                ((MouseHandler.Press) lookup).onPress(xMouse, yMouse, button);
            Widget.setFocused(lookup);
            Widget.pressedWidget = lookup;
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
            final Widget widget = Widget.pressedWidget;
            if (button == -1) {
                if (widget instanceof MouseHandler.Move)
                    ((MouseHandler.Move) widget).onMove(xMouse, yMouse, button);
            } else {
                if (widget instanceof MouseHandler.Release)
                    ((MouseHandler.Release) widget).onRelease(xMouse, yMouse, button);

                // If move mouse away click don't counts
                final Widget lookup = scene.lookupWidget(new Point(xMouse, yMouse)).orElse(null);
                if (lookup == Widget.pressedWidget) {
                    if (Widget.pressedWidget == widget && widget instanceof MouseHandler.Click)
                        ((MouseHandler.Click) widget).onClick(xMouse, yMouse, button);
                }

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
