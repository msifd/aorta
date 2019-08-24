package msifeed.mc.mellow.widgets.input;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.KeyHandler;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.input.Keyboard;

public class TextArea extends Widget implements KeyHandler, MouseHandler.Click {
    protected Part normalPart = Mellow.getPart("sunken");
    protected Part focusedPart = Mellow.getPart("sunken_focused");
    protected int darkColor = Mellow.getColor("text_dark");
    protected int brightColor = Mellow.getColor("text_bright");

    private TextController controller = new TextController();
    private long lastTimePressed = 0;

    public TextArea() {
        setSizeHint(10, 13);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
    }

    @Override
    protected void renderSelf() {
        try {
            renderBackground();
            renderText();
            if (isFocused())
                renderCursor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void renderBackground() {
        RenderParts.nineSlice(isFocused() ? focusedPart : normalPart, getGeometry());
    }

    protected void renderText() {
        if (controller.isEmpty())
            return;

        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        final Geom textGeom = new Geom(getGeometry());
        textGeom.translate(3, 2, 1);

        final int lineHeight = lineHeight(fr);
        controller.toLinesStream().forEach(line -> {
            RenderWidgets.string(textGeom, line, darkColor);
            textGeom.y += lineHeight;
        });
    }

    protected void renderCursor() {
        if ((System.currentTimeMillis() - lastTimePressed) % 1000 > 500)
            return;

        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        final String subline = controller.getCurrentLine().substring(0, controller.getCurColumn());

        final Geom cursorGeom = new Geom(getGeometry());
        cursorGeom.x += 3;
        cursorGeom.y += 4 + lineHeight(fr) * controller.getCurLine();
        cursorGeom.w = 0;
        cursorGeom.h = lineHeight(fr) - 1;
        cursorGeom.translate(fr.getStringWidth(subline), 0, 1);

        RenderShapes.line(cursorGeom, 3, 0);
    }

    @Override
    public void onKeyboard(char c, int key) {
        switch (c) {
//            case 1:
//                this.setCursorPositionEnd();
//                this.setSelectionPos(0);
//                return true;
            case 3:
                GuiScreen.setClipboardString(controller.toJoinedString());
                return;
            case 22:
                controller.input(GuiScreen.getClipboardString());
                return;
//            case 24:
//                GuiScreen.setClipboardString(this.getSelectedText());
//
//                if (this.isEnabled) {
//                    this.writeText("");
//                }
//
//                return true;
        }

        switch (key) {
            case Keyboard.KEY_LEFT:
                controller.moveCursorColumn(false);
                break;
            case Keyboard.KEY_RIGHT:
                controller.moveCursorColumn(true);
                break;
            case Keyboard.KEY_UP:
                controller.moveCursorLine(false);
                break;
            case Keyboard.KEY_DOWN:
                controller.moveCursorLine(true);
                break;
            case Keyboard.KEY_DELETE:
                controller.remove(true);
                break;
            case Keyboard.KEY_BACK:
                controller.remove(false);
                break;
            case Keyboard.KEY_RETURN:
                controller.breakLine();
                break;
            default:
                controller.input(c);
                lastTimePressed = System.currentTimeMillis();
                break;
        }
    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {
        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        final Geom geom = getGeometry();

        final int lineHeight = lineHeight(fr);
        final int line = (yMouse - geom.y - lineHeight / 2) / lineHeight;
        final int tune = 2;
        final int column = fr.trimStringToWidth(controller.getCurrentLine().toString(), xMouse - geom.x - tune).length();

        controller.moveCursor(line, column);
    }

    private int lineHeight(FontRenderer fr) {
        return fr.FONT_HEIGHT - 2;
    }
}
